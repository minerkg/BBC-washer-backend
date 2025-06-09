package com.bcc.washer.domain;

import java.time.LocalTime;

public enum OPENINGHOURS {



    OPEN(LocalTime.of(8, 0)),
    CLOSE(LocalTime.of(20, 0));

    private final LocalTime time;

    private OPENINGHOURS(LocalTime openingHours) {
        this.time = openingHours;
    }

    public LocalTime getTime() {
        return time;
    }
}
