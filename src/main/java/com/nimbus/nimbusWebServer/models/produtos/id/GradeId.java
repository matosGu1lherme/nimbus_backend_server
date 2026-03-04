package com.nimbus.nimbusWebServer.models.produtos.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GradeId {
    private Long produtoId;
    private String numeracao;
}
