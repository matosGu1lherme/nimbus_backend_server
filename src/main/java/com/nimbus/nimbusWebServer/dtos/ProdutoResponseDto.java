package com.nimbus.nimbusWebServer.dtos;

import java.math.BigDecimal;

public record ProdutoResponseDto(
        Long id,
        String nome,
        String slug,
        String descricao,
        BigDecimal preco,
        Long tipo_id,
        Long categoria_id,
        String sku
) { }