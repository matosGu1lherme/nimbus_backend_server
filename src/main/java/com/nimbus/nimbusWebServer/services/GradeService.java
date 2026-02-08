package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.models.produtos.Grade;
import com.nimbus.nimbusWebServer.models.produtos.Produto;
import com.nimbus.nimbusWebServer.repositories.GradeRepository;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    private GradeRepository gradeRepository;

    public GradeService(
            GradeRepository gradeRepository
    ){
        this.gradeRepository = gradeRepository;
    }

    public void incluirGradeProduto(Produto produto, String numeracao) {
        Grade novaGrade = new Grade(produto, numeracao);
        gradeRepository.save(novaGrade);
    }
}
