package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProdutoModel, UUID> {
}
