package com.nimbus.nimbusWebServer.dtos;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProdutoResponseDto(
        Long id,
        String nome,
        String slug,
        String descricao,
        BigDecimal preco,
        Long tipo_id,
        Long categoria_id,
        String sku,
        List<String> grade
) { }