package com.nimbus.nimbusWebServer.models.pedido;

import com.nimbus.nimbusWebServer.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "CARRINHO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Carrinho {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrinhoItem> carrinhoItems = new ArrayList<>();
}
