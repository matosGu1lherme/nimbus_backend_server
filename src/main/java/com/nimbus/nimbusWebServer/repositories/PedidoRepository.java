package com.nimbus.nimbusWebServer.repositories;

import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    List<Pedido> findByUsuarioId(UUID usuarioId);

    Optional<Pedido> findByIdempotencyKey(String idempotencyKey);
}
