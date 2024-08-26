package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.ProductImagesModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImagesModel, Long> {
}
