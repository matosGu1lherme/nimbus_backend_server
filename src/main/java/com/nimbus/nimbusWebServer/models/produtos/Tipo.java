package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TIPO")
@Getter
@Setter
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank
    @Column(length = 3, nullable = false, unique = true)
    private String abreviacao;

    @Column(nullable = false)
    private boolean ativo;

    @OneToMany(mappedBy = "tipo")
    private List<Produto> produtos;
}
