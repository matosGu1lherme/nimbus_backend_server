package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.AccessTokenResponseDto;
import com.nimbus.nimbusWebServer.dtos.UserDataInfoTokenDto;
import com.nimbus.nimbusWebServer.services.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private RefreshTokenService refreshTokenService;

    public AuthController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDto> atualizarAccessToken(
            @CookieValue("refreshToken") String refreshToken
    ) {
        String newAccessToken = refreshTokenService.refreshAccessToken(refreshToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AccessTokenResponseDto(newAccessToken));
    }

    @GetMapping("/me")
    public ResponseEntity<?> obterDadosUserPorCookie(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        if(refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sessão expirada ou inexistente.");
        }

        try {
            UserDataInfoTokenDto userDataInfoTokenDto = refreshTokenService.findUserInfoByToken(refreshToken);
            return ResponseEntity.ok(userDataInfoTokenDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido.");
        }
    }
 }
