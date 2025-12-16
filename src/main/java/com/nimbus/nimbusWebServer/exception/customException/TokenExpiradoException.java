package com.nimbus.nimbusWebServer.exception.customException;

import java.time.Instant;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException(Instant expiration) {
        super("Token expirado, validade atingida em " + expiration.toString());
    }
}
