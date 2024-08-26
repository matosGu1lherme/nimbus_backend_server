package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "PRODUTO_IMAGENS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImagesModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_image;

    @ManyToOne
    @JoinColumn(name = "id_produto", referencedColumnName = "id_produto")
    private ProdutoModel produto;

    @Column(name = "ordem_apresentacao")
    private Number ordem_apresentacao;

    @Column(name = "path_imagem")
    private String path_imagem;

}
