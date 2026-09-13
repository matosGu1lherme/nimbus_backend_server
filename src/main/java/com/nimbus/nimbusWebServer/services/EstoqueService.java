package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.AtualizarEstoqueRequestDto;
import com.nimbus.nimbusWebServer.exception.customException.RecursoNaoEncontradoException;
import com.nimbus.nimbusWebServer.models.pedido.Pedido;
import com.nimbus.nimbusWebServer.models.pedido.PedidoItem;
import com.nimbus.nimbusWebServer.models.produtos.Estoque;
import com.nimbus.nimbusWebServer.models.produtos.EstoqueGrade;
import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.models.produtos.id.GradeId;
import com.nimbus.nimbusWebServer.repositories.EstoqueGradeRepository;
import com.nimbus.nimbusWebServer.repositories.EstoqueRepository;
import com.nimbus.nimbusWebServer.repositories.GradeRepository;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final EstoqueGradeRepository estoqueGradeRepository;
    private final GradeRepository gradeRepository;

    public EstoqueService(
            ProdutoRepository produtoRepository,
            EstoqueRepository estoqueRepository,
            EstoqueGradeRepository estoqueGradeRepository,
            GradeRepository gradeRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
        this.estoqueGradeRepository = estoqueGradeRepository;
        this.gradeRepository = gradeRepository;
    }

    @Transactional
    public void atualizarEstoque(AtualizarEstoqueRequestDto dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + dto.produtoId()));

        Estoque estoque = estoqueRepository.findByProdutoId(dto.produtoId())
                .orElseGet(() -> {
                    Estoque novoEstoque = new Estoque();
                    novoEstoque.setProduto(produto);
                    return estoqueRepository.save(novoEstoque);
                });

        for (AtualizarEstoqueRequestDto.EstoqueGradeItemDto item : dto.estoquePorGrade()) {
            GradeId gradeId = new GradeId(dto.produtoId(), item.grade());

            Grade grade = gradeRepository.findById(gradeId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Grade " + item.grade() + " não encontrada para o produto " + dto.produtoId()));

            EstoqueGrade estoqueGrade = estoqueGradeRepository.findById(gradeId)
                    .orElseGet(() -> new EstoqueGrade(estoque, grade, 0));

            int quantidadeAtual = estoqueGrade.getQuantidade() == null ? 0 : estoqueGrade.getQuantidade();
            estoqueGrade.setQuantidade(quantidadeAtual + item.quantidade());
            estoqueGradeRepository.save(estoqueGrade);
        }

        recalcularQuantidadeTotal(estoque);
    }

    @Transactional
    public void baixarEstoquePorPedido(Pedido pedido) {
        for (PedidoItem item : pedido.getItens()) {
            baixarEstoque(item.getProduto().getId(), item.getGrade(), item.getQuantidade());
        }
    }

    @Transactional
    public void baixarEstoque(Long produtoId, String grade, Integer quantidadeVendida) {
        GradeId gradeId = new GradeId(produtoId, grade);

        EstoqueGrade estoqueGrade = estoqueGradeRepository.findById(gradeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estoque não encontrado para o produto " + produtoId + " na grade " + grade));

        int quantidadeAtual = estoqueGrade.getQuantidade() == null ? 0 : estoqueGrade.getQuantidade();
        if (quantidadeAtual < quantidadeVendida) {
            throw new IllegalStateException(
                    "Estoque insuficiente para o produto " + produtoId + " na grade " + grade
                            + " (disponível: " + quantidadeAtual + ", vendido: " + quantidadeVendida + ")");
        }

        estoqueGrade.setQuantidade(quantidadeAtual - quantidadeVendida);
        estoqueGradeRepository.save(estoqueGrade);

        recalcularQuantidadeTotal(estoqueGrade.getEstoque());
    }

    private void recalcularQuantidadeTotal(Estoque estoque) {
        List<EstoqueGrade> todasAsGrades = estoqueGradeRepository.findByEstoqueId(estoque.getId());

        int quantidadeTotal = todasAsGrades.stream()
                .mapToInt(eg -> eg.getQuantidade() == null ? 0 : eg.getQuantidade())
                .sum();

        estoque.setQuantidade(quantidadeTotal);
        estoque.setAtualizadoEm(LocalDateTime.now());
        estoqueRepository.save(estoque);
    }
}
