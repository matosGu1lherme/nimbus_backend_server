package com.nimbus.nimbusWebServer.dtos;

import java.math.BigDecimal;

public record ItemPedidoDto(
        Long idProduto,
        String grade,
        Integer quantidade,
        BigDecimal precoNoMomento
) {
}
