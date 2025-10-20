package com.nimbus.nimbusWebServer.exception.customException;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Usuário não encontrado com ID:" + id);
    }
}
