package com.nimbus.nimbusWebServer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PedidoMercadoPagoDto(
        String type,
        @JsonProperty("external_reference") String externalReference,
        @JsonProperty("total_amount") String totalAmount,
        @JsonProperty("processing_mode") String processingMode,
        PagadorPedido payer,
        Transacoes transactions
) {
    public record Transacoes(List<TransacaoPagamento> payments) {}

    public record TransacaoPagamento(
            String amount,
            @JsonProperty("payment_method") FormaPagamento paymentMethod
    ) {}
}