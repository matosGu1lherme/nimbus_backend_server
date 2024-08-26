package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ProductDto;
import com.nimbus.nimbusWebServer.exception.customs.CustomDatabaseException;
import com.nimbus.nimbusWebServer.models.ProductImagesModel;
import com.nimbus.nimbusWebServer.models.ProdutoModel;
import com.nimbus.nimbusWebServer.repositories.ProductImagesRepository;
import com.nimbus.nimbusWebServer.repositories.ProductRepository;
import com.nimbus.nimbusWebServer.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImagesRepository productImagesRepository;

    @Transactional
    public void saveProduct(ProductDto productDto){
        ProdutoModel produto = ProductUtils.makeProduct(productDto);
        productRepository.save(produto);

        List<ProductImagesModel> images = ProductUtils.makeImages(produto, productDto.images());
        productImagesRepository.saveAll(images);
    }

}
