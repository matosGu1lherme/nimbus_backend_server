package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PRODUTOS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id_produto")
    private UUID id_produto;
    @Column(name = "nome_produto")
    private String nome;
    @Column(name = "descricao_produto")
    private String descricao;
    @Column(name = "valor_produto")
    private BigDecimal valor;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ProductImagesModel> images;

}
