package com.nimbus.nimbusWebServer.exception.customs;

import lombok.Getter;

@Getter
public class CustomDatabaseException extends RuntimeException{

    private final String devMessage;
    private final String methodName;
    private final String classname;

    public CustomDatabaseException(String message, String devMessage, String methodName, String classname) {
        super(message);
        this.devMessage = devMessage;
        this.methodName = methodName;
        this.classname = classname;
    }
}
