package com.nimbus.nimbusWebServer.models.produtos.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
public class ImagemId implements Serializable {
    private Long produtoId;
    private Integer sequencia;
}
