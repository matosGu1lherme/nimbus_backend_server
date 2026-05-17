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


 }
