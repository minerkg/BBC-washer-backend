package com.bcc.washer.exceptions;

public class WasherStoreException extends RuntimeException{
    public WasherStoreException(String message) {
        super(message);
    }

    public WasherStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    protected WasherStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
