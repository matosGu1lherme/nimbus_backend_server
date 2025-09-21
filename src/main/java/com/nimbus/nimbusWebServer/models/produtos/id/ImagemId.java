package com.nimbus.nimbusWebServer.models.produtos.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImagemId implements Serializable {
    private Long produtoId;
    private Integer sequencia;
}
