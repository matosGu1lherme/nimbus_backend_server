package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long produtoId);
}
