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
    String statusDetalhe,
    BigDecimal valorTotal,
    List<ItemPedidoResponseDto> itens,
    DadosPagamentoDto dadosPagamento
) {
    @Builder
    public record ItemPedidoResponseDto(
            Long produtoId,
            String nome,
            Integer quantidade,
            BigDecimal precoNoMomento,

            @JsonProperty("url_imagem")
            String urlimagem
    ) {}

    @Builder
    public record DadosPagamentoDto(
            String qrCode,
            String qrCodeBase64,
            String digitableLine,
            String ticketUrl,
            String redirectUrl
    ) {}
}
