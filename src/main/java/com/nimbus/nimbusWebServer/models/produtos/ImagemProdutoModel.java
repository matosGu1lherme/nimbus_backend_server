package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "IMAGEM_PRODUTO")
public class ImagemProdutoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Number ordem_apresentacao;

    private String url;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;
}
