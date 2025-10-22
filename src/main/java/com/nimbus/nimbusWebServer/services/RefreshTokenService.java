package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.exception.customException.UserNotFoundException;
import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.user.RefreshToken;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.RefreshTokenRepository;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository
    ) {
            this.refreshTokenRepository = refreshTokenRepository;
            this.userRepository = userRepository;
    }

    public RefreshToken criarRefreshToken(UserDetailsImpl user) {
        String token = gerarToken();
        String hashToken = geraTokenHash(token);
        User usuario = userRepository.findById(user.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(user.getUser().getId()));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(hashToken);
        refreshToken.setCriacao(Instant.now());
        refreshToken.setExpiração(Instant.now().plus(24, ChronoUnit.HOURS));
        refreshToken.setRevogado(false);
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }
    
    public String gerarToken() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    public String geraTokenHash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash token", e);
        }
    }
}
