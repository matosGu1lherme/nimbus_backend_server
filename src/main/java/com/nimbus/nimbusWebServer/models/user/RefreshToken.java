package com.nimbus.nimbusWebServer.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    private String token;

    private Instant expiração;

    private Instant criacao;

    private Boolean revogado;
}
