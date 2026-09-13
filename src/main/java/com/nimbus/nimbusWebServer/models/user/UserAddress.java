package com.nimbus.nimbusWebServer.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.processing.SQL;

@Table(name = "user_address")
@Entity(name = "UserAddress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String logradouro;

    private Integer numero;

    private String bairro;

    private String cidade;

    private String cep;

    private String pais;

    private Boolean principal = true;

    private Boolean inCancelado = false;
}
