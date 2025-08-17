package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "TAGS")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany(mappedBy = "tags")
    private List<Produto> produtos;
}
