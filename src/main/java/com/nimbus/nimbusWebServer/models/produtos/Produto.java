package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PRODUTO")
@Getter
@Setter
public class Produto implements Serializable {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(unique = true)
    private String sku;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Imagem> imagens = new ArrayList<>();

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
