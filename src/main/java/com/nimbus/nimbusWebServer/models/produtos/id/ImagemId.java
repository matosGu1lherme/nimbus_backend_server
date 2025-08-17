package com.nimbus.nimbusWebServer.models.produtos.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ImagemId implements Serializable {
    private Long produtoId;
    private Integer sequencia;
}
