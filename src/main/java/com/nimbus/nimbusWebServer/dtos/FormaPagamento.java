package com.nimbus.nimbusWebServer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FormaPagamento(
        String id,
        String type,
        String token,
        Integer installments,
        @JsonProperty("issuer_id") String issuerId
) {}
