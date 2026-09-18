package com.nimbus.nimbusWebServer.services;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.mercadopago.resources.order.OrderPayment;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.dtos.PedidoResponseDto;
import com.nimbus.nimbusWebServer.enums.MetodoPagamento;
import com.nimbus.nimbusWebServer.enums.StatusPedido;
import com.nimbus.nimbusWebServer.models.pedido.CarrinhoItem;
import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import com.nimbus.nimbusWebServer.models.pedido.PedidoItem;
import com.nimbus.nimbusWebServer.models.user.UserAddress;
import com.nimbus.nimbusWebServer.repositories.PedidoRepository;
import com.nimbus.nimbusWebServer.repositories.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PedidoService {
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private EstoqueService estoqueService;

    public PedidoResponseDto processarPagamento(CheckoutRequestDto checkoutRequestDto, String idempotencyKey) throws MPException, MPApiException {
        Optional<Pedido> pedidoExistente = pedidoRepository.findByIdempotencyKey(idempotencyKey);

        if(pedidoExistente.isPresent()) {
            return montarResponse(pedidoExistente.get());
        }

        Pedido pedido;
        try {
            pedido = criarPedido(checkoutRequestDto, idempotencyKey);
        }catch (DataIntegrityViolationException e) {
            return pedidoRepository.findByIdempotencyKey(idempotencyKey)
                    .map(this::montarResponse)
                    .orElseThrow(() -> e);
        }

        //Enviando pagamento para o mercado pago
        Order order = mercadoPagoService.finalizarCompraMp(checkoutRequestDto, idempotencyKey);

        //Atualizando o pedido de acordo com o oder
        OrderPayment ultimoPagamento = order.getTransactions().getPayments().getLast();
        pedido.setStatusPedido(Pedido.traduzStatusMP(ultimoPagamento.getStatus(), ultimoPagamento.getStatusDetail()));
        pedido.setStatusDetalhe(ultimoPagamento.getStatusDetail());
        pedido.setOrderId(order.getId());
        pedido.preencherDadosPagamento(ultimoPagamento.getPaymentMethod());
        pedidoRepository.save(pedido);

        if(pedido.getStatusPedido() == StatusPedido.AGUARDANDO_PAGAMENTO
                || pedido.getStatusPedido() == StatusPedido.APROVADO
                || pedido.getStatusPedido() == StatusPedido.AUTORIZADO) {
            carrinhoService.limparCarrinho(UUID.fromString(checkoutRequestDto.usuarioId()));
        }

        return montarResponse(pedido);
    }

    public List<PedidoResponseDto> buscarPedidosUsuario(UUID usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::montarResponse)
                .toList();
    }

    public PedidoResponseDto buscarPedidoPorId(UUID idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Não foi possivel encontrar o pedido solicitado."));

        return montarResponse(pedido);
    }

    public BigDecimal retornaPrecoTotalPedido(Pedido pedido) {
         return  pedido.getItens()
                .stream()
                .map((pedidoItem) -> pedidoItem.getPrecoUnitario().multiply(BigDecimal.valueOf(pedidoItem.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected Pedido criarPedido(CheckoutRequestDto dto, String idempotencyKey) {
        List<CarrinhoItem> itensPedido = carrinhoService.buscarCarrinhoItensEntidadePorId(UUID.fromString(dto.usuarioId()));

        BigDecimal totalValorCarrinhoBD = itensPedido.stream()
                .map(item -> item.getValorMomentoCompra().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(!totalValorCarrinhoBD.equals(dto.amount())){
            throw new RuntimeException("Valor presente no carrinho do usuario difere do montante total enviada para pagamento");
        }

        Pedido pedido = Pedido.gerarPedido(dto, itensPedido);
        pedido.setIdempotencyKey(idempotencyKey);

        UserAddress enderecoEntrega = userAddressRepository.getReferenceById(dto.idEnderecoEnvio());
        pedido.setEderecoEntrega(enderecoEntrega);

        return pedidoRepository.save(pedido);
    }

    private PedidoResponseDto montarResponse(Pedido pedido) {
        PedidoResponseDto.DadosPagamentoDto dadosPagamento = PedidoResponseDto.DadosPagamentoDto.builder()
                .qrCode(pedido.getQrCode())
                .qrCodeBase64(pedido.getQrCodeBase64())
                .digitableLine(pedido.getDigitableLine())
                .ticketUrl(pedido.getTicketUrl())
                .redirectUrl(pedido.getRedirectUrl())
                .build();

        List<PedidoResponseDto.ItemPedidoResponseDto> itens = pedido.getItens().stream()
                .map(item -> PedidoResponseDto.ItemPedidoResponseDto.builder()
                        .produtoId(item.getProduto().getId())
                        .nome(item.getProduto().getNome())
                        .quantidade(item.getQuantidade())
                        .precoNoMomento(item.getPrecoUnitario())
                        .urlimagem(item.getProduto().getImagens().isEmpty() ? null : item.getProduto().getImagens().getFirst().getUrl())
                        .build())
                .toList();

        return PedidoResponseDto.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .dataCriacao(pedido.getCriadoEm())
                .status(pedido.getStatusPedido())
                .statusDetalhe(pedido.getStatusDetalhe())
                .valorTotal(retornaPrecoTotalPedido(pedido))
                .itens(itens)
                .dadosPagamento(dadosPagamento)
                .build();
    }
}
