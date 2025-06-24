package com.bcc.washer.exceptions;

public class WasherAlreadyExistsException extends WasherStoreException{
    public WasherAlreadyExistsException(String message) {
        super(message);
    }

    public WasherAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    protected WasherAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
