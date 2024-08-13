package com.nimbus.nimbusWebServer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Entity
@Table(name = "PEDIDOS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PedidoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long numero_pedido;

}
