package com.nimbus.nimbusWebServer.dtos;

public record UserAddressResponseDto(
        Long id,
        String logradouro,
        Integer numero,
        String bairro,
        String cidade,
        String cep,
        String pais,
        boolean principal
) {
}
