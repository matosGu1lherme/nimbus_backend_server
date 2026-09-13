package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.security.roles.RoleName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CreateUserDto(
        String nome,
        String email,
        String password,
        String cpf,
        Date data_nascimento,
        RoleName role,

        @NotNull(message = "endereco é obrigatório")
        @Valid
        UserAddressDto endereco
) {
}
