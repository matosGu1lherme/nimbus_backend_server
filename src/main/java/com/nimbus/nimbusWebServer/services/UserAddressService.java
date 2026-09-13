package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.dtos.UserAddressDto;
import com.nimbus.nimbusWebServer.dtos.UserAddressResponseDto;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.models.user.UserAddress;
import com.nimbus.nimbusWebServer.repositories.UserAddressRepository;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserRepository userRepository;

    public UserAddressResponseDto criarEndereco(UUID userId, UserAddressDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        UserAddress endereco = new UserAddress();
        endereco.setUser(user);
        preencherEndereco(endereco, dto);
        endereco.setPrincipal(userAddressRepository.findByUserId(userId).isEmpty());

        return montarResponse(userAddressRepository.save(endereco));
    }

    public UserAddressResponseDto definirEnderecoPrincipal(UUID userId, Long enderecoId) {
        List<UserAddress> enderecos = userAddressRepository.findByUserId(userId);

        UserAddress novoPrincipal = enderecos.stream()
                .filter(endereco -> endereco.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para este usuário."));

        enderecos.forEach(endereco -> endereco.setPrincipal(endereco.getId().equals(enderecoId)));
        userAddressRepository.saveAll(enderecos);

        return montarResponse(novoPrincipal);
    }

    public List<UserAddressResponseDto> listarEnderecos(UUID userId) {
        return userAddressRepository.findByInCanceladoFalseAndUserId(userId).stream()
                .map(this::montarResponse)
                .toList();
    }

    public UserAddressResponseDto atualizarEndereco(UUID userId, Long enderecoId, UserAddressDto dto) {
        UserAddress endereco = userAddressRepository.findByIdAndUserId(enderecoId, userId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para este usuário."));

        preencherEndereco(endereco, dto);

        return montarResponse(userAddressRepository.save(endereco));
    }

    public void deletarEndereco(UUID userId, Long enderecoId) {
        UserAddress endereco = userAddressRepository.findByIdAndUserId(enderecoId, userId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para este usuário."));

        boolean eraPrincipal = endereco.getPrincipal();
        userAddressRepository.delete(endereco);

        if (eraPrincipal) {
            userAddressRepository.findByUserId(userId).stream()
                    .findFirst()
                    .ifPresent(proximoPrincipal -> {
                        proximoPrincipal.setPrincipal(true);
                        userAddressRepository.save(proximoPrincipal);
                    });
        }
    }

    private void preencherEndereco(UserAddress endereco, UserAddressDto dto) {
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setCep(dto.cep());
        endereco.setPais(dto.pais() != null ? dto.pais() : "Brasil");
    }

    private UserAddressResponseDto montarResponse(UserAddress endereco) {
        return new UserAddressResponseDto(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getCep(),
                endereco.getPais(),
                endereco.getPrincipal()
        );
    }
}
