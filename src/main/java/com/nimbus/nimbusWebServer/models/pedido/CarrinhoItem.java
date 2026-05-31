package com.nimbus.nimbusWebServer.models.pedido;

import com.nimbus.nimbusWebServer.models.pedido.Id.CarrinhoItemId;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CARRINHO_ITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoItem {

    @EmbeddedId
    private CarrinhoItemId id =  new CarrinhoItemId();

    @ManyToOne
    @MapsId("carrinhoId")
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
    private Double valorMomentoCompra;
}
