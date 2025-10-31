package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.AccessTokenResponseDto;
import com.nimbus.nimbusWebServer.dtos.RefreshTokenRequestDto;
import com.nimbus.nimbusWebServer.services.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private RefreshTokenService refreshTokenService;

    public AuthController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDto> atualizarAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        String newAccessToken = refreshTokenService.refreshAccessToken(refreshTokenRequestDto.refreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AccessTokenResponseDto(newAccessToken));
    }
}
