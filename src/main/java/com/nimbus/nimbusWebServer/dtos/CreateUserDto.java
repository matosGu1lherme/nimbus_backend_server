package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.security.roles.RoleName;

import java.util.Date;

public record CreateUserDto(
        String nome,
        String email,
        String password,
        String cpf,
        Date data_nascimento,
        RoleName role
) {
}
