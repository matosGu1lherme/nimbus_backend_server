package com.nimbus.nimbusWebServer.controllers;


import com.nimbus.nimbusWebServer.dtos.AuthResponseDto;
import com.nimbus.nimbusWebServer.dtos.CreateUserDto;
import com.nimbus.nimbusWebServer.dtos.LoginResponseDto;
import com.nimbus.nimbusWebServer.dtos.LoginUserDto;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${cookie.params-secure}")
    private Boolean cookieSecure;

    @Value("${cookie.params-sameSite}")
    private String cookieSameSite;

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        String refreshToken = userService.authenticateUser(loginUserDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Autenticado com sucesso!");
    }



}