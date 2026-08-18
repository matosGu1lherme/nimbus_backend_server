package com.nimbus.nimbusWebServer.services;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.dtos.ResponsePedidoDto;
import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    @Autowired
    private MercadoPagoService mercadoPagoService;

    public ResponsePedidoDto processarPagamento(CheckoutRequestDto checkoutRequestDto) throws MPException, MPApiException {
        Order order = mercadoPagoService.finalizarCompraMp(checkoutRequestDto);

        Pedido pedido = criarPedidoPeloStatus(checkoutRequestDto, order);

        return new ResponsePedidoDto(pedido.getNumeroPedido(), pedido.getStatusPedido(), pedido.getStatusDetalhe());
    }

    @Transactional
    protected Pedido criarPedidoPeloStatus(CheckoutRequestDto dto, Order order) {
        Pedido pedido new Pedido.gerarPedido(dto, order);
    }
}
