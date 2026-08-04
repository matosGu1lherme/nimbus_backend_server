package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.CheckoutMercadoPagoRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

    @PostMapping("/gerar_pedido")
    public ResponseEntity<?> enviarPagamento(@RequestBody CheckoutMercadoPagoRequestDto checkoutMpDto) {

    }
}
