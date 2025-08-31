package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SKU_SEQUENCE")
@Getter
@Setter
@NoArgsConstructor
public class SkuSequence {

    @Id
    private String chave;

    @Column(nullable = false)
    private int contador;

    @Version
    private Long version;
}
