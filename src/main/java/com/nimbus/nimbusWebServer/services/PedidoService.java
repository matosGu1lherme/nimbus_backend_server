package com.nimbus.nimbusWebServer.services;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.dtos.PedidoResponseDto;
import com.nimbus.nimbusWebServer.dtos.ResponsePedidoDto;
import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import com.nimbus.nimbusWebServer.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class PedidoService {
    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    public ResponsePedidoDto processarPagamento(CheckoutRequestDto checkoutRequestDto) throws MPException, MPApiException {
        Pedido pedido = criarPedido(checkoutRequestDto);

        Order order = mercadoPagoService.finalizarCompraMp(checkoutRequestDto);

        return new ResponsePedidoDto(pedido.getNumeroPedido(), pedido.getStatusPedido(), pedido.getStatusDetalhe());
    }

    public List<PedidoResponseDto> buscarPedidosUsuario(UUID usuarioId) {
        List<Pedido> pedidosUser = pedidoRepository.findByUsuarioId(usuarioId);

        if(pedidosUser.isEmpty()) {
            return new ArrayList<>();
        }

        List<PedidoResponseDto> pedidoList = new ArrayList<>();
        for(Pedido pedido : pedidosUser) {
            PedidoResponseDto pedidoResponseDto = PedidoResponseDto.builder()
                    .id(pedido.getId())
                    .numeroPedido(pedido.getNumeroPedido())
                    .dataCriacao(pedido.getCriadoEm())
                    .status(pedido.getStatusPedido())
                    .valorTotal(retornaPrecoTotalPedido(pedido))
                    .itens(pedido.getItens().stream().map(item -> PedidoResponseDto.ItemPedidoResponseDto.builder()
                            .produtoId(item.getProduto().getId())
                            .nome(item.getProduto().getNome())
                            .quantidade(item.getQuantidade())
                            .precoNoMomento(item.getPrecoUnitario())
                            .build())
                            .toList())
                    .build();

            pedidoList.add(pedidoResponseDto);
        }

        return pedidoList;
    }

    public BigDecimal retornaPrecoTotalPedido(Pedido pedido) {
         return  pedido.getItens()
                .stream()
                .map((pedidoItem) -> pedidoItem.getPrecoUnitario().multiply(BigDecimal.valueOf(pedidoItem.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    protected Pedido criarPedido(CheckoutRequestDto dto) {
        Pedido pedido = Pedido.gerarPedido(dto);
        return pedidoRepository.save(pedido);
    }
}
