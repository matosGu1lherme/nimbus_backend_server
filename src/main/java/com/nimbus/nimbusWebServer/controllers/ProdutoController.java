package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.ProdutoRequestDto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.services.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public  ProdutoController (ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/salvar_produto")
    public String criarProduto(
            @RequestPart("produto") ProdutoRequestDto produtoJson,
            @RequestPart("imagens") MultipartFile[] imagens) {
        produtoService.salvarProduto(produtoJson, imagens);
        return("Produto salvo com sucesso!");
    }

    @GetMapping("/buscarTodosProdutos")
    public ResponseEntity<List<Produto>> buscarTodosProdutos() {
        List<Produto> produtos = produtoService.buscarTodosProdutos();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(produtos);
    }
}
