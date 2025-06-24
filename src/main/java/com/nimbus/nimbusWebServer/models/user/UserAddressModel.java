package com.nimbus.nimbusWebServer.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_address")
@Entity(name = "UserAddress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    private String lagradouro;

    private Integer numero;

    private String bairro;

    private String cidade;

    private String cep;

    private String pais;

}
