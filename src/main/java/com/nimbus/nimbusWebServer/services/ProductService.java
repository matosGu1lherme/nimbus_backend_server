package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.repositories.ProductImagesRepository;
import com.nimbus.nimbusWebServer.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImagesRepository productImagesRepository;



}
