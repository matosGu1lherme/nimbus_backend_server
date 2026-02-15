package com.nimbus.nimbusWebServer.models.produtos;

import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;

@Getter
@Entity
public class Grade {
    @EmbeddedId
    private GradeId id;

    @ManyToOne
    @MapsId("produtoId")
    private Produto produto;

    private Boolean ativo;

    public Grade(Produto produto, String numeracao) {
        this.id = new GradeId(produto.getId(), numeracao);
        this.produto = produto;
        this.ativo = true;
    }
}
