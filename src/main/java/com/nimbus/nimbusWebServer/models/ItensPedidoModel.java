package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_pedido")
public class ItensPedidoModel {

    @EmbeddedId
    private ItensPedidoIdModel id;

    @ManyToOne
    @MapsId("numero_pedido")
    @JoinColumn(name = "numero_pedido")
    private PedidoModel pedido;

    @ManyToOne
    @MapsId("produto_id")
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

    @Column(name = "quantidade_produto")
    private Integer quantidade;
}
