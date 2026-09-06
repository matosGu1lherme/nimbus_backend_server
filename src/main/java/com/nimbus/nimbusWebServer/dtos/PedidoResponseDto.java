package com.nimbus.nimbusWebServer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbus.nimbusWebServer.enums.StatusPedido;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record PedidoResponseDto(
    UUID id,
    Long numeroPedido,
    Instant dataCriacao,
    StatusPedido status,
    BigDecimal valorTotal,
    List<ItemPedidoResponseDto> itens
) {
    @Builder
    public record ItemPedidoResponseDto(
            Long produtoId,
            String nome,
            Integer quantidade,
            BigDecimal precoNoMomento,

            @JsonProperty("url_imagem")
            String urlimagem
    ) {};
}
