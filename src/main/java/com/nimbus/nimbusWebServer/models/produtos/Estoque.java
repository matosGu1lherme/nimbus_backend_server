package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ESTOQUE")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    private LocalDateTime atualizadoEm = LocalDateTime.now();
}