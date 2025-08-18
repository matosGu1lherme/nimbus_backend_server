package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.models.produtos.Tipo;
import com.nimbus.nimbusWebServer.repositories.TipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService {

    private final TipoRepository tipoRepository;

    public TipoService( TipoRepository tipoRepository ) { this.tipoRepository = tipoRepository; }

    public Tipo salvarTipo(Tipo tipo) {
        return tipoRepository.save(tipo);
    }

    public List<Tipo> listarTipos() {
        return tipoRepository.findAll();
    }
}
