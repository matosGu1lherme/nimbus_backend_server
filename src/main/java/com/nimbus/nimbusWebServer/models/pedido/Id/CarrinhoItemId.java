package com.nimbus.nimbusWebServer.models.pedido.Id;

import com.nimbus.nimbusWebServer.models.produtos.Produto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoItemId {

    private UUID userId;
    private Long produtoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarrinhoItemId that = (CarrinhoItemId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, produtoId);
    }
}
