package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.ProdutoNumeracaoDto;
import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.repositories.GradeRepository;
import com.nimbus.nimbusWebServer.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService {

    private GradeRepository gradeRepository;
    private ProdutoRepository produtoRepository;

    public GradeService(
            GradeRepository gradeRepository,
            ProdutoRepository produtoRepository
    ){
        this.gradeRepository = gradeRepository;
        this.produtoRepository = produtoRepository;
    }

    public void incluirGradeProduto(List<ProdutoNumeracaoDto> produtoNumeracaoDto) {
        List<Grade> gradeList = new ArrayList<>();
        for(ProdutoNumeracaoDto item : produtoNumeracaoDto) {
            Grade novaGrade = new Grade(item.produto(), item.numeracao());
            gradeList.add(novaGrade);
        }

        gradeRepository.saveAll(gradeList);
    }

    public List<String> buscarGradeProduto(Long produtoId) {
        List<Grade> gradeList = gradeRepository.findByProdutoId(produtoId);
        List<String> numercaoList = gradeList.stream()
                .map(gradeItem -> gradeItem.getId().getNumeracao())
                .toList();
        return numercaoList;
    }
}
