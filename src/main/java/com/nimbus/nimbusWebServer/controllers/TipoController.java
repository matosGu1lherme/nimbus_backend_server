package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.models.produtos.Tipo;
import com.nimbus.nimbusWebServer.services.TipoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo")
public class TipoController {

    private final TipoService tipoService;

    public TipoController(TipoService tipoService) { this.tipoService = tipoService; }

    @PostMapping("salvar_tipo")
    public ResponseEntity<Tipo> criarTipo(@RequestBody Tipo tipo) {
        Tipo novoTipo = tipoService.salvarTipo(tipo);
        return ResponseEntity.ok(novoTipo);
    }

    @GetMapping("listar_tipos")
    public List<Tipo> listarTipos() {
        return tipoService.listarTipos();
    }
}
