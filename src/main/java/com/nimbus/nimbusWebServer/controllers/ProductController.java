package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.ProductDto;
import com.nimbus.nimbusWebServer.models.ProdutoModel;
import com.nimbus.nimbusWebServer.repositories.ProductRepository;
import com.nimbus.nimbusWebServer.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @PostMapping("/save_product")
    public ResponseEntity<String> saveProduct(@RequestBody @Valid ProductDto productDto){
        productService.saveProduct(productDto);
        return new ResponseEntity<>("Produto salvo com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/get_all_products")
    public ResponseEntity<List<ProdutoModel>> getAllProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/get_product/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        Optional<ProdutoModel> product = productRepository.findById(id);
        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/update_product/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductDto productDto){
        Optional<ProdutoModel> product = productRepository.findById(id);
        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = product.get();
        BeanUtils.copyProperties(productDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping("/delete_product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        Optional<ProdutoModel> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productRepository.delete(product.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product delete successfully.");
    }
}
