package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.security.roles.RoleName;

public record CreateUserDto(
        String email,
        String password,
        RoleName role
) {
}
