// src/main/java/com/bcc/washer/domain/ReservationStatus.java
package com.bcc.washer.domain.reservation;

public enum ReservationStatus {
    PENDING,        // Optionally, if there's a confirmation step
    CONFIRMED,      // The booking is active and confirmed
    CANCELLED,      // The booking has been cancelled by user or admin
    COMPLETED       // The booked time slot has passed
}