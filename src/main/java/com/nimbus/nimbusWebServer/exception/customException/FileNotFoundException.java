package com.nimbus.nimbusWebServer.exception.customException;

import lombok.Getter;

@Getter
public class FileNotFoundException extends RuntimeException {
    private final String filename;

    public FileNotFoundException(String message, String filename, Throwable cause) {
        super(message, cause);
        this.filename = filename;
    }

    public FileNotFoundException(String message, String filename) {
        super(message);
        this.filename = filename;
    }
}
