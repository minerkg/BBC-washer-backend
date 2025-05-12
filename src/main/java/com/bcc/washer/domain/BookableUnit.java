package com.bcc.washer.domain;

public class BookableUnit {


    private Washer washer;
    private boolean isAvailable;

    public BookableUnit(Washer washer) {
        this.washer = washer;
        this.isAvailable = true;
    }

    public void setUnavailable() {
        this.isAvailable = false;
    }

    public Washer getWasher() {
        return washer;
    }
    public boolean getIsAvailable() {
        return isAvailable;
    }
}
