package com.nimbus.nimbusWebServer.models.pedido;

import com.mercadopago.resources.order.Order;
import com.mercadopago.resources.payment.PaymentStatus;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.enums.MetodoPagamento;
import com.nimbus.nimbusWebServer.enums.StatusPedido;
import com.nimbus.nimbusWebServer.enums.TipoDocumentoComprador;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "numero_pedido", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_numero_pedido")
    @SequenceGenerator(name = "seq_numero_pedido", sequenceName = "numero_pedido_seq", allocationSize = 1)
    private Long numeroPedido;

    private String servicoPagamento;

    @Column(name = "order_id", unique = true)
    private String orderId;

    private String paymentId;

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

    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant pagoEm;
    private Instant expiraEm;

    private static StatusPedido traduzStatusMP(String statusMercadoPago) {
        return switch (statusMercadoPago) {
            case PaymentStatus.APPROVED -> StatusPedido.APROVADO;
            case PaymentStatus.AUTHORIZED -> StatusPedido.AGUARDANDO_PAGAMENTO;
            case PaymentStatus.IN_PROCESS, PaymentStatus.PENDING -> StatusPedido.AGUARDANDO_PAGAMENTO;
            case PaymentStatus.REJECTED -> StatusPedido.RECUSADO;
            case PaymentStatus.CANCELLED -> StatusPedido.CANCELADO;
            case PaymentStatus.REFUNDED, PaymentStatus.CHARGED_BACK -> StatusPedido.CANCELADO;
            case PaymentStatus.IN_MEDIATION -> StatusPedido.AGUARDANDO_PAGAMENTO;
            default -> throw new IllegalStateException(
                    "Não foi encontrada tradução para o status Mercado Pago recebido: [%s]".formatted(statusMercadoPago)
            );
        };
    }

    public static Pedido gerarPedido(CheckoutRequestDto dto, Order order) {
        Pedido novoPedido = new Pedido();

        novoPedido.setServicoPagamento(dto.paymentMethod());

        novoPedido.setStatusPedido(traduzStatusMP( order.getStatus()));
    }
}

