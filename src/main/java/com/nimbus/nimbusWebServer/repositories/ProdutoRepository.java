package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaNome(String categoria);
    Optional<Produto> findBySlug(String slug);
}
