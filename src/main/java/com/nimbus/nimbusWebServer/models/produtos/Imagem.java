package com.nimbus.nimbusWebServer.models.produtos;

import com.nimbus.nimbusWebServer.models.produtos.id.ImagemId;
import jakarta.persistence.*;

@Entity
@Table(name = "IMAGEM_PRODUTO")
public class Imagem {
    @EmbeddedId
    private ImagemId id;

    @MapsId("produtoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private String url;
}
