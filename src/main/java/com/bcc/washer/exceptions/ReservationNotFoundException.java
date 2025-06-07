// src/main/java/com/bcc/washer/exceptions/ReservationNotFoundException.java
package com.bcc.washer.exceptions;

public class ReservationNotFoundException extends WasherStoreException {
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}