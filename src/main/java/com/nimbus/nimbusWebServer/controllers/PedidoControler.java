package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.dtos.PedidoResponseDto;
import com.nimbus.nimbusWebServer.repositories.PedidoRepository;
import com.nimbus.nimbusWebServer.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pedido")
public class PedidoControler {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @GetMapping("/buscar_pedidos_usuario/{idUsario}")
    public ResponseEntity<?> buscarPedidosUsuario(@PathVariable String idUsario) {
        List<PedidoResponseDto> pedidoResponseDtoList = pedidoService.buscarPedidosUsuario(UUID.fromString(idUsario));
        return ResponseEntity.ok(pedidoResponseDtoList);
    }

    @GetMapping("/buscar_pedido/{idPedido}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable String idPedido) {
        PedidoResponseDto pedidoResponseDto = pedidoService.buscarPedidoPorId(UUID.fromString(idPedido));
        return ResponseEntity.ok(pedidoResponseDto);
    }
}
