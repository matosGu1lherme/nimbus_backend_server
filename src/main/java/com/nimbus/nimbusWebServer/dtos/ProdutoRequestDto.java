package com.nimbus.nimbusWebServer.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRequestDto(
        String nome,
        String descricao,
        BigDecimal preco,
        Long categoriaId,
        List<Long> tagIds,
        List<String> images,
        Long estoqueId
) { }
