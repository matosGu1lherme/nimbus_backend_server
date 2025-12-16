package com.nimbus.nimbusWebServer.models.produtos;

import com.nimbus.nimbusWebServer.dtos.TipoRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TIPO")
@Getter
@Setter
@NoArgsConstructor
public class Tipo {

    public Tipo(TipoRequestDto tipoRequestDto) {
        this.nome = tipoRequestDto.nome();
        this.abreviacao = tipoRequestDto.abreviacao();
        this.ativo = tipoRequestDto.ativo();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank
    @Column(length = 3, nullable = false, unique = true)
    private String abreviacao;

    @Column(nullable = false)
    private boolean ativo;

    @OneToMany(mappedBy = "tipo")
    private List<Produto> produtos;

    private LocalDateTime criadoEm = LocalDateTime.now();
}
