package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.models.produtos.SkuSequence;
import com.nimbus.nimbusWebServer.repositories.SkuSequenceRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkuSequenceService {

    private final SkuSequenceRepository skuSequenceRepository;

    public SkuSequenceService(SkuSequenceRepository skuSequenceRepository) {
        this.skuSequenceRepository = skuSequenceRepository;
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3
    )
    public String gerarSkuSequence(String chave) {
        SkuSequence sequence = skuSequenceRepository.findById(chave)
                .orElseGet(() -> {
                   SkuSequence nova = new SkuSequence();
                   nova.setChave(chave);
                   nova.setContador(0);
                   return skuSequenceRepository.save(nova);
                });
        sequence.setContador(sequence.getContador() + 1);
        skuSequenceRepository.save(sequence);

        return chave + "-" + String.format("%03d", sequence.getContador());
    }
}
