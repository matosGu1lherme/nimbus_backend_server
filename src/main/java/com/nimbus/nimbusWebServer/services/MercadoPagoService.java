package com.nimbus.nimbusWebServer.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.order.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.nimbus.nimbusWebServer.config.properties.MercadoPagoProperties;
import com.nimbus.nimbusWebServer.dtos.CheckoutMercadoPagoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MercadoPagoService {

    @Autowired
    private MercadoPagoProperties mpConfig;

    public Order finalizarCompraMp(CheckoutMercadoPagoRequestDto checkoutMpDto) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(mpConfig.accessToken());

        // Monta o método de pagamento
        var paymentMethod = OrderPaymentMethodRequest.builder()
                .type(checkoutMpDto.paymentMethod())
                .id(checkoutMpDto.paymentMethodId())
                .token(checkoutMpDto.token())
                .installments(checkoutMpDto.Installments())
                .build();

        var payment = OrderPaymentRequest.builder()
                .amount(checkoutMpDto.amount().toPlainString())
                .paymentMethod(paymentMethod)
                .build();

        var payer = OrderPayerRequest.builder()
                .email(checkoutMpDto.payer().email())
                .build();

        var orderRequest = OrderCreateRequest.builder()
                .type("online")
                .totalAmount(checkoutMpDto.amount().toPlainString())
                .externalReference(UUID.randomUUID().toString())
                .payer(payer)
                .transactions(OrderTransactionRequest.builder()
                        .payments(List.of(payment))
                        .build())
                .build();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Idempotency-Key", UUID.randomUUID().toString());
        var requesOptions = MPRequestOptions.builder()
                .customHeaders(headers)
                .build();

        var client = new OrderClient();
        return client.create(orderRequest, requesOptions);
    }
}
