package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.models.produtos.Produto;

public record ProdutoNumeracaoDto(
        Produto produto,
        String numeracao
) {
}
