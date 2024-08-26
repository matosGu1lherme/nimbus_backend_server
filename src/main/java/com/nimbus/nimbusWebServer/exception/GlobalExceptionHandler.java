package com.nimbus.nimbusWebServer.exception;

import com.nimbus.nimbusWebServer.exception.customs.CustomDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomDatabaseException.class)
    public ResponseEntity<String> handleCustomDatabaseException(CustomDatabaseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
