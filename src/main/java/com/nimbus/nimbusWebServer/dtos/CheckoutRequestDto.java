package com.nimbus.nimbusWebServer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CheckoutRequestDto(
        @NotBlank(message = "paymentMethod é obrigatório")
        @Pattern(regexp = "credit_card|pix|boleto", message = "paymentMethod deve ser credit_card, pix ou boleto")
        String paymentMethod,       // "credit_card" | "pix" | "boleto"

        @NotNull(message = "amount é obrigatório")
        @DecimalMin(value = "0.01", message = "amount deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "payer é obrigatório")
        @Valid
        Payer payer,

        Long idEnderecoEnvio,

        String token,
        Integer installments,
        String paymentMethodId,     // bandeira (master, visa, etc.)
        String issuerId,
        String usuarioId,

        List<ItemPedidoDto> itensOrdemPedido
) {
    public record Payer(String email, String name, Identification identification) {}

    public record Identification(String type, String number) {}
}
