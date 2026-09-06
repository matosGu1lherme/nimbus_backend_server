package com.nimbus.nimbusWebServer.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.dtos.ResponsePedidoDto;
import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import com.nimbus.nimbusWebServer.services.PedidoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private PedidoService pedidoService;

    @PostMapping("/finalizar_compra")
    public ResponseEntity<?> finalizarPagamento(@Valid @RequestBody CheckoutRequestDto checkoutMpDto) {
        try {
            ResponsePedidoDto responsePedidoDto = pedidoService.processarPagamento(checkoutMpDto);

            return ResponseEntity.ok("Pedido processado" + responsePedidoDto);
        } catch (MPException e) {
            log.error("Erro no Mercado Pago: ", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao processar pagamento");
        } catch (MPApiException e) {
            log.error("Erro na API do Mercado Pago: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro de comunicação com o mercado pago");
        } catch (Exception e) {
            log.error("Erro inesperado ao efetivarPagamento", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao finalizarPagamento");
        }
    }
}
