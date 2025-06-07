// src/main/java/com/bcc/washer/exceptions/BookableUnitNotAvailableException.java
package com.bcc.washer.exceptions;

public class BookableUnitNotAvailableException extends WasherStoreException {
    public BookableUnitNotAvailableException(String message) {
        super(message);
    }

    public BookableUnitNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}