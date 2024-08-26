package com.nimbus.nimbusWebServer.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProductImagesDto(
        Number ordem_apresentacao,
        String path_imagem
)
{}
