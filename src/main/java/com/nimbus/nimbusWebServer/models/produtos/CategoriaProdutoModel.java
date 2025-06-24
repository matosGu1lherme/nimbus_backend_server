package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CATEGORIA_PRODUTO")
public class CategoriaProdutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    private String descricao;

    private Boolean ativa;

    @ManyToOne
    private CategoriaProdutoModel categoriaPai; //Criação da arvore de categorias

    @ManyToMany(mappedBy = "categorias")
    private Set<ProdutoModel> produtos = new HashSet<>();
}
