package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.models.produtos.Categoria;
import com.nimbus.nimbusWebServer.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping("salvar_categoria")
    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.salvarCategoria(categoria);
        return ResponseEntity.ok(novaCategoria);
    }

    @GetMapping("public/buscar_categorias")
    public List<Categoria> buscarCategorias() {
        return categoriaService.listarCategorias();
    }
}
