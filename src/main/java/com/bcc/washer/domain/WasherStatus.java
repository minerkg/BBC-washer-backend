// src/main/java/com/bcc/washer/domain/WasherStatus.java
package com.bcc.washer.domain;

public enum WasherStatus {
    AVAILABLE,       // Ready for booking / not currently in use
    IN_USE,          // Currently being used for a confirmed booking
    MAINTENANCE      // Out of order for maintenance
}