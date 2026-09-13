package com.nimbus.nimbusWebServer.mapper;

import com.nimbus.nimbusWebServer.dtos.ProdutoResponseDto;
import com.nimbus.nimbusWebServer.models.produtos.Estoque;
import com.nimbus.nimbusWebServer.models.produtos.EstoqueGrade;
import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.ImagemProduto;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {

    @Value("${nimbus.parcelas.maximo-sem-juros}")
    private Integer quantidadeMaximaParcelasSemJuros;

    @Value("${nimbus.entrega.prazo-dias-uteis}")
    private Integer prazoEntregaDiasUteis;

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
                .img_url(mapImgApresentacao(produto.getImagens()))
                // Tratando os IDs de forma segura para evitar NullPointerException
                .tipo_id(Optional.ofNullable(produto.getTipo())
                        .map(t -> t.getId())
                        .orElse(null))
                .categoria_id(Optional.ofNullable(produto.getCategoria())
                        .map(c -> c.getId())
                        .orElse(null))
                .categoriaNome(Optional.ofNullable(produto.getCategoria())
                        .map(c -> c.getNome())
                        .orElse(null))
                .marca(produto.getMarca())
                .precoAntigo(produto.getPrecoAntigo())
                .avaliacaoMedia(produto.getAvaliacaoMedia())
                .quantidadeAvaliacoes(produto.getQuantidadeAvaliacoes())
                .quantidadeEstoque(Optional.ofNullable(produto.getEstoque())
                        .map(Estoque::getQuantidade)
                        .orElse(0))
                // Chamando o método da grade
                .grade(mapGradeList(produto.getGrade()))
                .estoquePorGrade(mapEstoquePorGrade(produto.getGrade(), produto.getEstoque()))
                .quantidadeMaximaParcelasSemJuros(quantidadeMaximaParcelasSemJuros)
                .prazoEntregaDiasUteis(prazoEntregaDiasUteis)
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

    private List<ProdutoResponseDto.EstoqueGradeDto> mapEstoquePorGrade(List<Grade> grade, Estoque estoque) {
        if (grade == null) {
            return Collections.emptyList();
        }

        List<EstoqueGrade> estoquePorGrade = estoque != null && estoque.getEstoquePorGrade() != null
                ? estoque.getEstoquePorGrade()
                : Collections.emptyList();

        Map<String, Integer> quantidadePorNumeracao = estoquePorGrade.stream()
                .filter(eg -> eg != null && eg.getId() != null)
                .collect(Collectors.toMap(
                        eg -> eg.getId().getNumeracao(),
                        eg -> eg.getQuantidade() == null ? 0 : eg.getQuantidade()
                ));

        return grade.stream()
                .filter(g -> g != null && g.getId() != null)
                .map(g -> new ProdutoResponseDto.EstoqueGradeDto(
                        g.getId().getNumeracao(),
                        quantidadePorNumeracao.getOrDefault(g.getId().getNumeracao(), 0)))
                .toList();
    }

    private String mapImgApresentacao(List<ImagemProduto> imagens) {
        if (imagens == null) {
            return null;
        }

        return imagens.stream()
                .filter(img -> img != null && img.getId() != null && img.getId().getSequencia() == 1)
                .map(ImagemProduto::getUrl)
                .findFirst()
                .orElse(null);
    }
}
