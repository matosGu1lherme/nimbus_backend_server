package com.nimbus.nimbusWebServer.models.produtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CATEGORIA")
@Getter
@Setter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank
    @Column(length = 3, nullable = false, unique = true)
    private String abreviacao;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
