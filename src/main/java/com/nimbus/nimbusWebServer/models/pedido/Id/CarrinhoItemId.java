package com.nimbus.nimbusWebServer.models.pedido.Id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarrinhoItemId {

    private Long carrinhoId;
    private Long produtoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarrinhoItemId that = (CarrinhoItemId) o;
        return Objects.equals(carrinhoId, that.carrinhoId) &&
                Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrinhoId, produtoId);
    }
}
