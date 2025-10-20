package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
