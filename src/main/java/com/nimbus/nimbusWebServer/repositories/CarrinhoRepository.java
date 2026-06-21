package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.pedido.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> { }
