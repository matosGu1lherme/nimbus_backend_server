package com.nimbus.nimbusWebServer.dtos;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProdutoResponseDto(
        Long id,
        String nome,
        String slug,
        String descricao,
        BigDecimal preco,
        Long tipo_id,
        Long categoria_id,
        String sku,
        String img_url,
        List<String> grade,
        String categoriaNome,
        String marca,
        BigDecimal precoAntigo,
        Double avaliacaoMedia,
        Integer quantidadeAvaliacoes,
        Integer quantidadeEstoque,
        List<EstoqueGradeDto> estoquePorGrade,
        Integer quantidadeMaximaParcelasSemJuros,
        Integer prazoEntregaDiasUteis
) {
    @Builder
    public record EstoqueGradeDto(String grade, Integer quantidade) { }
}
