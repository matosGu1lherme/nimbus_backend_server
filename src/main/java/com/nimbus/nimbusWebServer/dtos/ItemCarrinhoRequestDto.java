package com.nimbus.nimbusWebServer.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCarrinhoRequestDto(
        String email,
        Long produtoId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @NotNull(message = "Valor no momento da compra é obrigatório")
        @Positive(message = "Valor no momento da compra é obrigatório")
        Double valorMomentoCompra
) {
}
