package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.AtualizarEstoqueRequestDto;
import com.nimbus.nimbusWebServer.dtos.ProdutoRequestDto;
import com.nimbus.nimbusWebServer.dtos.ProdutoResponseDto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.services.EstoqueService;
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
    private final EstoqueService estoqueService;

    public  ProdutoController (ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @PostMapping("/atualizar_estoque")
    public ResponseEntity<String> atualizarEstoque(@RequestBody AtualizarEstoqueRequestDto dto) {
        estoqueService.atualizarEstoque(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Estoque atualizado com sucesso!");
    }

    @PostMapping("/salvar_produto")
    public String criarProduto(
            @RequestPart("produto") ProdutoRequestDto produtoJson,
            @RequestPart("imagens") MultipartFile[] imagens
    ) {
        produtoService.salvarProduto(produtoJson, imagens);
        return("Produto salvo com sucesso!");
    }

    @GetMapping("/public/buscar_produtos")
    public ResponseEntity<List<ProdutoResponseDto>> buscarTodosProdutos() {
        List<ProdutoResponseDto> produtos = produtoService.buscarProdutos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtos);
    }

    @GetMapping("/public/buscar_produtos_por_nome_categoria")
    public ResponseEntity<List<ProdutoResponseDto>> buscarProdutosPorNomeCategoria(@RequestParam String categoriaNome) {
        List<ProdutoResponseDto> produtos = produtoService.buscarProdutosPorNomeCategoria(categoriaNome);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtos);
    }

    @GetMapping("/public/buscar_imagem_apresentacao_produto")
    public String buscarImagemApresentacaoProduto(@RequestParam Long produtoId) {
        return produtoService.retornarImgApresentacaoProd(produtoId);
    }

    @GetMapping("/public/buscar_imagens_produto")
    public ResponseEntity<List<String>> buscarTodasImagensProduto(@RequestParam Long produtoId) {
        List<String> listaImgsProd = produtoService.retornarImagensProduto(produtoId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listaImgsProd);
    }

    @GetMapping("/public/obter_produto_por_slug")
    public ResponseEntity<ProdutoResponseDto> buscarProdutoPorSlug(@RequestParam String slug) {
        ProdutoResponseDto produtoResponseDto = produtoService.buscarProdutoPorSlug(slug);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoResponseDto);
    }
}
