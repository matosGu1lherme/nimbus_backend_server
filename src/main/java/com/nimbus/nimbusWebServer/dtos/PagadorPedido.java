package com.nimbus.nimbusWebServer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagadorPedido(
        String email,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        Identificacao identification
) {
    public record Identificacao(String type, String number) {}
}
