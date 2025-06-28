package com.bcc.washer.domain;

public class ResourceNotExists extends RuntimeException {
    public ResourceNotExists(String message) {
        super(message);
    }
}
