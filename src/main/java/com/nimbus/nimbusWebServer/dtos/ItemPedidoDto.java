package com.nimbus.nimbusWebServer.dtos;

import java.math.BigDecimal;

public record ItemPedidoDto(
        Long idProduto,
        Integer quantidade,
        BigDecimal precoNoMomento
) {
}
