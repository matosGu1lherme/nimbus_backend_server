package com.nimbus.nimbusWebServer.models.pedido;

import com.nimbus.nimbusWebServer.dtos.ItemPedidoDto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "PEDIDO_ITEM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    public static PedidoItem criarItemDto(ItemPedidoDto dto, Pedido pedido) {
        PedidoItem pedidoItem = new PedidoItem();

        Produto produtoItem = new Produto();
        produtoItem.setId(dto.idProduto());

        pedidoItem.setPedido(pedido);
        pedidoItem.setProduto(produtoItem);
        pedidoItem.setGrade(dto.grade());
        pedidoItem.setQuantidade(dto.quantidade());
        pedidoItem.setPrecoUnitario(dto.precoNoMomento());

        return pedidoItem;
    }
}
