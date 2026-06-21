package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.pedido.CarrinhoItem;
import com.nimbus.nimbusWebServer.models.pedido.Id.CarrinhoItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, CarrinhoItemId> {
    Optional<CarrinhoItem> findByCarrinhoUserIdAndProdutoId(UUID userId, Long produtoId);
}
