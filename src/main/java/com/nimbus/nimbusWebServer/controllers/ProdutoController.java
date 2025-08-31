package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.ProdutoRequestDto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.services.ProdutoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
