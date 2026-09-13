package com.nimbus.nimbusWebServer.models.produtos;

import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ESTOQUE_GRADE")
@Getter
@Setter
@NoArgsConstructor
public class EstoqueGrade {

    @EmbeddedId
    private GradeId id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "produto_id", referencedColumnName = "produto_id"),
            @JoinColumn(name = "numeracao", referencedColumnName = "numeracao")
    })
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @Version
    private Long version;

    private LocalDateTime atualizadoEm = LocalDateTime.now();

    public EstoqueGrade(Estoque estoque, Grade grade, Integer quantidade) {
        this.id = grade.getId();
        this.estoque = estoque;
        this.grade = grade;
        this.quantidade = quantidade;
    }
}
