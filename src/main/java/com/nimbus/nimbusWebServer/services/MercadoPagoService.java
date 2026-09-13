package com.nimbus.nimbusWebServer.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.order.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.Order;
import com.nimbus.nimbusWebServer.config.properties.MercadoPagoProperties;
import com.nimbus.nimbusWebServer.dtos.CheckoutRequestDto;
import com.nimbus.nimbusWebServer.enums.MetodoPagamento;
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

    public Order finalizarCompraMp(CheckoutRequestDto checkoutMpDto, String idempotencyKey) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken(mpConfig.accessToken());

        // Monta o método de pagamento
        var paymentMethod = montarPaymentMethod(checkoutMpDto);

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
        headers.put("X-Idempotency-Key", idempotencyKey);
        var requestOptions = MPRequestOptions.builder()
                .customHeaders(headers)
                .build();

        var client = new OrderClient();
        return client.create(orderRequest, requestOptions);
    }

    private OrderPaymentMethodRequest montarPaymentMethod(CheckoutRequestDto dto) {
        MetodoPagamento metodoPagamento = MetodoPagamento.tranformaMetodoPagamento(dto.paymentMethod());

        var builder = OrderPaymentMethodRequest.builder()
                .type(metodoPagamento.getMpType())
                .id(metodoPagamento.getMpId() != null ? metodoPagamento.getMpId() : dto.paymentMethodId());

        if (metodoPagamento == MetodoPagamento.CREDIT_CARD) {
            builder.token(dto.token())
                    .installments(dto.installments());
        }

        return builder.build();
    }
}
