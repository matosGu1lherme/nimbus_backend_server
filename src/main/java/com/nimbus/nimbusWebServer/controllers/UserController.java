package com.nimbus.nimbusWebServer.controllers;


import com.nimbus.nimbusWebServer.dtos.AuthResponseDto;
import com.nimbus.nimbusWebServer.dtos.CreateUserDto;
import com.nimbus.nimbusWebServer.dtos.LoginUserDto;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
import com.nimbus.nimbusWebServer.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        String refreshToken = userService.authenticateUser(loginUserDto);
        String accessToken = accessTokenService.generateToken(loginUserDto.email());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponseDto(refreshToken, accessToken));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}