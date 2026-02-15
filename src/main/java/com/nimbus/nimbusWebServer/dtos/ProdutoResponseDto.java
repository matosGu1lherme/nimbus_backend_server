package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.models.produtos.Grade;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoResponseDto(
        Long id,
        String nome,
        String slug,
        String descricao,
        BigDecimal preco,
        Long tipo_id,
        Long categoria_id,
        String sku,
        List<Grade> numeracoesGrade
) { }