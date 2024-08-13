package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "PRODUTO_IMAGENS")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductImagesModel implements Serializable {

    @Id
    @ManyToOne
    @MapsId
    @JoinColumn(name = "id_produto")
    private ProdutoModel produto;

    @Column(name = "ordem_apresentacao")
    private Number ordem_apresentacao;

    @Column(name = "path_imagem")
    private String path_imagem;

}
