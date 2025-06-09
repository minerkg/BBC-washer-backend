package com.bcc.washer.domain;

public class ResourceAlreadyExistsException extends RuntimeException {


    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
