package com.nimbus.nimbusWebServer.dtos;

public record ResponsePedidoDto(
        String numeroPedidom,
        String statusPedido,
        String statusDetalhe
) {
}
