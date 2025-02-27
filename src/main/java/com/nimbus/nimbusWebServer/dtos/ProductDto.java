package com.nimbus.nimbusWebServer.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        @NotBlank String nome,
        String descricao,
        @NotNull BigDecimal valor,
        List<ProductImagesDto> images
)
{}
