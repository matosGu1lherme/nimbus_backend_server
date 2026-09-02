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
        Pedido pedido = criarPedido(checkoutRequestDto);

        Order order = mercadoPagoService.finalizarCompraMp(checkoutRequestDto);

        return new ResponsePedidoDto(pedido.getNumeroPedido(), pedido.getStatusPedido(), pedido.getStatusDetalhe());
    }

    @Transactional
    protected Pedido criarPedido(CheckoutRequestDto dto) {
        Pedido pedido = Pedido.gerarPedido(dto);
        return pedido;
    }
}
