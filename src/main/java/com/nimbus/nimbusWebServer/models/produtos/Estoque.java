package com.nimbus.nimbusWebServer.models.produtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ESTOQUE")
@Getter
@Setter
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstoqueGrade> estoquePorGrade = new ArrayList<>();

    @Version
    private Long version;

    private LocalDateTime atualizadoEm = LocalDateTime.now();

    public void recalcularQuantidadeTotal() {
        this.quantidade = estoquePorGrade.stream()
                .mapToInt(eg -> eg.getQuantidade() == null ? 0 : eg.getQuantidade())
                .sum();
        this.atualizadoEm = LocalDateTime.now();
    }
}
