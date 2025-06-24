package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "PRODUTO")
public class ProdutoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    private String nome;

    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(unique = true)
    private String sku;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemProdutoModel> images;

    @ManyToMany
    @JoinTable(
            name = "produto_categoria_mn",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<CategoriaProdutoModel> categorias = new HashSet<>();
}
