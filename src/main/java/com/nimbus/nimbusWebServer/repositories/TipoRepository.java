package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoRepository extends JpaRepository<Tipo, Long> { }
