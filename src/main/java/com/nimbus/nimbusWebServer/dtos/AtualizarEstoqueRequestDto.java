package com.nimbus.nimbusWebServer.dtos;

import java.util.List;

public record AtualizarEstoqueRequestDto(
        Long produtoId,
        List<EstoqueGradeItemDto> estoquePorGrade
) {
    public record EstoqueGradeItemDto(String grade, Integer quantidade) { }
}
