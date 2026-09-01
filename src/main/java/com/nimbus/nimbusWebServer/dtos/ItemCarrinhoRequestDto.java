package com.nimbus.nimbusWebServer.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemCarrinhoRequestDto(
        UUID userId,
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @NotNull(message = "A tamanho é obrigatória")
        @Positive(message = "A tamanho deve ser maior que zero")
        Integer tamanho,

        @NotNull(message = "Valor no momento da compra é obrigatório")
        @Positive(message = "Valor no momento da compra é obrigatório")
        BigDecimal valorMomentoCompra
) {
}
