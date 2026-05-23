package com.nimbus.nimbusWebServer.controllers;


import com.nimbus.nimbusWebServer.dtos.*;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
import com.nimbus.nimbusWebServer.services.RefreshTokenService;
import com.nimbus.nimbusWebServer.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired AccessTokenService accessTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Value("${cookie.params-secure}")
    private Boolean cookieSecure;

    @Value("${cookie.params-sameSite}")
    private String cookieSameSite;

    @Value("${cookie.cookie-domain:}")
    private String cookieDomain;

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register_store_user")
    public ResponseEntity<Void> createStoreUser(@RequestBody CreateUserDto createUserDto) {
        userService.createStoreUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        String refreshToken = userService.authenticateUser(loginUserDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .domain(cookieDomain)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Map<String, String> resposta = new HashMap<>();
        resposta.put("message", "Autenticado com sucesso!");
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        return ResponseEntity.ok("Logout realizado com sucesso!");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> atualizarAccessToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        String newAccessToken = refreshTokenService.refreshAccessToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .domain(cookieDomain)
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Refresh realizado com sucesso!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> obterDadosUserPorCookie(
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        String email = accessTokenService.getSubjectFromToken(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Erro ao buscar usuario /me"));

        return ResponseEntity.ok(Map.of(
                "loggedIn", true,
                "email", email,
                "usuario", user.getNome(),
                "id", user.getId()
        ));
    }

}