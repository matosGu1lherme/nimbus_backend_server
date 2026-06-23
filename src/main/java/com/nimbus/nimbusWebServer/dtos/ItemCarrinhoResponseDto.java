package com.nimbus.nimbusWebServer.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemCarrinhoResponseDto(
        Long produtoId,
        String nome,
        Integer quantidade,
        BigDecimal preco,
        String url_imagem
) {
}
