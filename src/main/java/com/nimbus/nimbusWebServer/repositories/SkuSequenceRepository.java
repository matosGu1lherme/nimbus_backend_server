package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.produtos.SkuSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuSequenceRepository extends JpaRepository<SkuSequence, String> { }
