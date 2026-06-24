package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoRequestDto;
import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoResponseDto;
import com.nimbus.nimbusWebServer.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar_ao_carrinho")
    public ResponseEntity<?> adicionarItemCarrinho(@RequestBody ItemCarrinhoRequestDto itemCarrinhoRequestDto) {
        try {
            carrinhoService.adicionarItemCarrinho(itemCarrinhoRequestDto);
        } catch (RuntimeException e) {
            System.out.println(e.fillInStackTrace());;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/buscar_itens_carrinho/{id}")
    public ResponseEntity<?> buscarItensCarrinho(@PathVariable("userId") UUID id) {
        List<ItemCarrinhoResponseDto> response = carrinhoService.buscarItensCarrinhoPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
