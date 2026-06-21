package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.ItemCarrinhoRequstDto;
import com.nimbus.nimbusWebServer.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar_ao_carrinho")
    public ResponseEntity<?> adicionarItemCarrinho(@RequestBody ItemCarrinhoRequstDto itemCarrinhoRequstDto) {
        try {
            carrinhoService.adicionarItemCarrinho(itemCarrinhoRequstDto);
        } catch (RuntimeException e) {
            System.out.println(e.fillInStackTrace());;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> buscarItensCarrinho(@RequestBody)
}
