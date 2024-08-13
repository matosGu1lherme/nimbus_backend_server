package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItensPedidoIdModel {

    @Column(name = "numero_pedido")
    private long numero_pedido;

    @Column(name = "produto_id")
    private UUID produto_id;
}
