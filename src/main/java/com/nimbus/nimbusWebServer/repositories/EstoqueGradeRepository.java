package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.EstoqueGrade;
import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstoqueGradeRepository extends JpaRepository<EstoqueGrade, GradeId> {
    List<EstoqueGrade> findByEstoqueId(Long estoqueId);
}
