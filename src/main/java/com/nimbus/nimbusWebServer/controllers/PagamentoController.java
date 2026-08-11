package com.nimbus.nimbusWebServer.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.nimbus.nimbusWebServer.dtos.CheckoutMercadoPagoRequestDto;
import com.nimbus.nimbusWebServer.dtos.RespostaPedidoDto;
import com.nimbus.nimbusWebServer.services.MercadoPagoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @Autowired
    private MercadoPagoService mpService;

    @PostMapping("/finalizar_compra")
    public ResponseEntity<?> enviarPagamento(@Valid @RequestBody CheckoutMercadoPagoRequestDto checkoutMpDto) {
        try {
            Order order = mpService.finalizarCompraMp(checkoutMpDto);

            return switch (order.getStatus()) {
                case "processed" -> ResponseEntity.ok(new RespostaPedidoDto(order.getId(), order.getStatus(), order.getStatusDetail()));
                default ->  ResponseEntity.ok(new RespostaPedidoDto(order.getId(), order.getStatus(), order.getStatusDetail()));
            };
        } catch (MPApiException e) {
            log.error("[MercadoPagoService] Erro da API MP: {}", e.getApiResponse().getContent());
            return ResponseEntity.badRequest().body("Não foi possível processar o pagamento");
        } catch (MPException e) {
            log.error("[MercadoPagoService] Erro de comunicação com MP", e);
            return ResponseEntity.internalServerError().body("Erro ao processar pagamento.");
        }
    }
}
