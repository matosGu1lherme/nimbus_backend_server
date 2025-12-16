package com.nimbus.nimbusWebServer.models.produtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUTO")
@Getter
@Setter
public class Produto implements Serializable {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String nome;

    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @NotNull
    @Column(unique = true, nullable = false)
    private String sku;

    @JsonManagedReference
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ImagemProduto> imagens = new ArrayList<>();

    @NotNull
    @JoinColumn(name = "tipo_id", nullable = false)
    @ManyToOne
    private Tipo tipo;

    @NotNull
    @JoinColumn(name = "categoria_id", nullable = false)
    @ManyToOne
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "PRODUTO_TAGS",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = false)
    private Estoque estoque;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
