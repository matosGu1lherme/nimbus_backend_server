package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT r FROM RefreshToken r JOIN FETCH r.usuario WHERE r.token = :token") //Busca o usuario junto com o token
    Optional<RefreshToken> findByToken(String token);
}
