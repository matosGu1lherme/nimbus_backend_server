package com.nimbus.nimbusWebServer.models.produtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbus.nimbusWebServer.dtos.CategoriaResquestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CATEGORIA")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    public Categoria(CategoriaResquestDto categoriaResquestDto) {
        this.nome = categoriaResquestDto.nome();
        this.abreviacao = categoriaResquestDto.abreviacao();
        this.ativo = categoriaResquestDto.ativo();
    }

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

    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
