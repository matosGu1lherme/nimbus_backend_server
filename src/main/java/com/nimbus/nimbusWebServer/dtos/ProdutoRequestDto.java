package com.nimbus.nimbusWebServer.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRequestDto(
        String nome,
        String descricao,
        BigDecimal preco,
        Long tipo_id,
        Long categoria_id
) { }
