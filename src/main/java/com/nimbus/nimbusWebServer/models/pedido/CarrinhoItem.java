package com.nimbus.nimbusWebServer.models.pedido;

import com.nimbus.nimbusWebServer.models.pedido.Id.CarrinhoItemId;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Type;

@Entity
@Table(name = "CARRINHO_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItem {

    @EmbeddedId
    private CarrinhoItemId id =  new CarrinhoItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
    private Double valorMomentoCompra;
}
