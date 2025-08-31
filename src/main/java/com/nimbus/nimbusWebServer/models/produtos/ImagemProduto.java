package com.nimbus.nimbusWebServer.models.produtos;

import com.nimbus.nimbusWebServer.models.produtos.id.ImagemId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUTO_IMAGEM")
@Getter
@Setter
public class ImagemProduto {
    @EmbeddedId
    private ImagemId id;

    @MapsId("produtoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private String url;
}
