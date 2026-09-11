package com.nimbus.nimbusWebServer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserAddressDto(
        @NotBlank(message = "logradouro é obrigatório")
        String logradouro,

        @NotNull(message = "numero é obrigatório")
        Integer numero,

        String bairro,

        @NotBlank(message = "cidade é obrigatório")
        String cidade,

        @NotBlank(message = "cep é obrigatório")
        String cep,

        String pais
) {
}
