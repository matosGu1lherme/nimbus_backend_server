package com.nimbus.nimbusWebServer.dtos;

import com.nimbus.nimbusWebServer.security.roles.Role;

import java.util.List;
import java.util.UUID;

public record RecoveryUserDto(
        UUID id,
        String email,
        List<Role> roles
) {
}
