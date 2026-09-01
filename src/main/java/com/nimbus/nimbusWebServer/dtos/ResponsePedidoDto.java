package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.enums.StatusPedido;

public record ResponsePedidoDto(
        Long numeroPedidom,
        StatusPedido statusPedido,
        String statusDetalhe
) {
}
