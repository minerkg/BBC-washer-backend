package com.bcc.washer.domain;

public enum TimeSlotDuration {

    ONE_HOUR(1),
    TWO_HOURS(2);

    private final int duration;

    TimeSlotDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }


}
