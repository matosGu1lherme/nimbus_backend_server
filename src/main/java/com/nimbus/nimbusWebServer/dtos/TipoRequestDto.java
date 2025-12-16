package com.nimbus.nimbusWebServer.dtos;

public record TipoRequestDto(
    String nome,
    String abreviacao,
    Boolean ativo
) { }
