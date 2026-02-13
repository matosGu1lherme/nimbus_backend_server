package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, GradeId> {
    List<Grade> findByProdutoId(Long produtoId);
}
