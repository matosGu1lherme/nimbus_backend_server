package com.nimbus.nimbusWebServer.models.pedido;

import com.mercadopago.resources.order.OrderPaymentMethod;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.dtos.ItemPedidoDto;
import com.nimbus.nimbusWebServer.enums.MetodoPagamento;
import com.nimbus.nimbusWebServer.enums.StatusPedido;
import com.nimbus.nimbusWebServer.enums.TipoDocumentoComprador;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.models.user.UserAddress;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PEDIDO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero_pedido", unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BIGSERIAL")
    private Long numeroPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(unique = true)
    private String idempotencyKey;

    private String servicoPagamento;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private String statusDetalhe;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private Integer parcelas;
    private String bandeira;

    private String compradorNome;
    private String compradorEmail;

    @Enumerated(EnumType.STRING)
    private TipoDocumentoComprador compradorTipoDocumento;

    private String compradorNumeroDocumento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoItem> itens = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant criadoEm;

    @UpdateTimestamp
    private Instant atualizadoEm;

    private Instant pagoEm;
    private Instant expiraEm;

    // Dados de pagamento (pix/boleto/3DS) devolvidos pelo Mercado Pago, persistidos
    // para que uma resposta idempotente (mesma X-Idempotency-Key) consiga devolvê-los de novo.
    @Column(columnDefinition = "TEXT")
    private String qrCode;

    @Column(columnDefinition = "TEXT")
    private String qrCodeBase64;

    private String digitableLine;

    @Column(columnDefinition = "TEXT")
    private String ticketUrl;

    @Column(columnDefinition = "TEXT")
    private String redirectUrl;

    @ManyToOne
    @JoinColumn(name = "endereco_entrega_id")
    private UserAddress ederecoEntrega;

    public void preencherDadosPagamento(OrderPaymentMethod metodoPagamento) {
        this.qrCode = metodoPagamento.getQrCode();
        this.qrCodeBase64 = metodoPagamento.getQrCodeBase64();
        this.digitableLine = metodoPagamento.getDigitableLine();
        this.ticketUrl = metodoPagamento.getTicketUrl();
        this.redirectUrl = metodoPagamento.getRedirectUrl();
    }

    // Vocabulário da Orders API (checkout-api-orders), não da Payments API legada:
    // https://www.mercadopago.com.ar/developers/en/docs/checkout-api-orders/payment-management/status/transaction-status
    public static StatusPedido traduzStatusMP(String statusMercadoPago, String statusDetailMercadoPago) {
        return switch (statusMercadoPago) {
            case "processed" -> StatusPedido.APROVADO;
            case "created", "processing", "in_review" -> StatusPedido.AGUARDANDO_PAGAMENTO;
            case "action_required" -> switch (statusDetailMercadoPago) {
                case "waiting_capture" -> StatusPedido.AUTORIZADO;
                default -> StatusPedido.AGUARDANDO_PAGAMENTO;
            };
            case "failed" -> StatusPedido.RECUSADO;
            case "canceled", "refunded", "charged_back" -> StatusPedido.CANCELADO;
            case "expired" -> StatusPedido.EXPIRADO;
            default -> throw new IllegalStateException(
                    "Não foi encontrada tradução para o status Mercado Pago recebido: [%s]".formatted(statusMercadoPago)
            );
        };
    }

    public static Pedido gerarPedido(CheckoutRequestDto dto, List<CarrinhoItem> itensPedidoCarrinho) {
        Pedido novoPedido = new Pedido();

        novoPedido.setServicoPagamento(dto.paymentMethod());

        novoPedido.setStatusPedido(StatusPedido.AGUARDANDO_PAGAMENTO);
        novoPedido.setStatusDetalhe("Criado pedido aguardando pagamento");
        novoPedido.setMetodoPagamento(MetodoPagamento.tranformaMetodoPagamento(dto.paymentMethod()));
        novoPedido.setParcelas(dto.installments());
        novoPedido.setBandeira(dto.paymentMethodId());

        User usuarioPedido = new User();
        usuarioPedido.setId(UUID.fromString(dto.usuarioId()));
        novoPedido.setUsuario(usuarioPedido);

        novoPedido.setCompradorNome(dto.payer().name());
        novoPedido.setCompradorEmail(dto.payer().email());
        novoPedido.setCompradorTipoDocumento(TipoDocumentoComprador.fromValor(dto.payer().identification().type()));
        novoPedido.setCompradorNumeroDocumento(dto.payer().identification().number());

        List<PedidoItem> itensPedido = new ArrayList<>();

        for (CarrinhoItem itemCarrinho : itensPedidoCarrinho) {
            PedidoItem novoItemPedido = PedidoItem.criarItemDto(itemCarrinho, novoPedido);
            itensPedido.add(novoItemPedido);
        }

        novoPedido.setItens(itensPedido);

        return  novoPedido;
    }
}

