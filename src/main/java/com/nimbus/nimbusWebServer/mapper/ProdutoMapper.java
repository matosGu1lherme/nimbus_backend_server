package com.nimbus.nimbusWebServer.mapper;

import com.nimbus.nimbusWebServer.dtos.ProdutoResponseDto;
import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ProdutoMapper {

    public ProdutoResponseDto toProdutoResponseDto(Produto produto) {
        if (produto == null) {
            return null;
        }

        return ProdutoResponseDto.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .sku(produto.getSku())
                .slug(produto.getSlug())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                // Tratando os IDs de forma segura para evitar NullPointerException
                .tipo_id(Optional.ofNullable(produto.getTipo())
                        .map(t -> t.getId())
                        .orElse(null))
                .categoria_id(Optional.ofNullable(produto.getCategoria())
                        .map(c -> c.getId())
                        .orElse(null))
                // Chamando o método da grade
                .grade(mapGradeList(produto.getGrade()))
                .build();
    }

    private List<String> mapGradeList(List<Grade> grade) {
        if (grade == null) {
            return Collections.emptyList();
        }

        return grade.stream()
                .filter(g -> g != null && g.getId() != null)
                .map(g -> g.getId().getNumeracao())
                .toList();
    }
}