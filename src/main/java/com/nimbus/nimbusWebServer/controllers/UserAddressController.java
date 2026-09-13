package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.UserAddressDto;
import com.nimbus.nimbusWebServer.dtos.UserAddressResponseDto;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
import com.nimbus.nimbusWebServer.services.UserAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users/address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private AccessTokenService accessTokenService;

    @PostMapping
    public ResponseEntity<UserAddressResponseDto> criarEndereco(
            @CookieValue("accessToken") String accessToken,
            @Valid @RequestBody UserAddressDto dto
    ) {
        UUID userId = accessTokenService.obterUserId(accessToken);
        return new ResponseEntity<>(userAddressService.criarEndereco(userId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/buscar_enderecos_ativos_usuario")
    public ResponseEntity<List<UserAddressResponseDto>> listarEnderecos(
            @CookieValue("accessToken") String accessToken
    ) {
        UUID userId = accessTokenService.obterUserId(accessToken);
        return ResponseEntity.ok(userAddressService.listarEnderecos(userId));
    }

    @PutMapping("/{enderecoId}")
    public ResponseEntity<UserAddressResponseDto> atualizarEndereco(
            @CookieValue("accessToken") String accessToken,
            @PathVariable Long enderecoId,
            @Valid @RequestBody UserAddressDto dto
    ) {
        UUID userId = accessTokenService.obterUserId(accessToken);
        return ResponseEntity.ok(userAddressService.atualizarEndereco(userId, enderecoId, dto));
    }

    @PatchMapping("/{enderecoId}/principal")
    public ResponseEntity<UserAddressResponseDto> definirEnderecoPrincipal(
            @CookieValue("accessToken") String accessToken,
            @PathVariable Long enderecoId
    ) {
        UUID userId = accessTokenService.obterUserId(accessToken);
        return ResponseEntity.ok(userAddressService.definirEnderecoPrincipal(userId, enderecoId));
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<Void> deletarEndereco(
            @CookieValue("accessToken") String accessToken,
            @PathVariable Long enderecoId
    ) {
        UUID userId = accessTokenService.obterUserId(accessToken);
        userAddressService.deletarEndereco(userId, enderecoId);
        return ResponseEntity.noContent().build();
    }
}
