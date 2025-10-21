package com.nimbus.nimbusWebServer.dtos;

public record AuthResponseDto(
        String refreshToken,
        String accessToken
) { }
