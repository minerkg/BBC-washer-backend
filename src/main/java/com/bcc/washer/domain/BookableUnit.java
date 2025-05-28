package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.sql.Time;

@Entity
public class BookableUnit {

    @Id
    private Long id;

    @ManyToOne
    private Washer washer;

    @ManyToOne
    private TimeSlot timeSlot;

    private boolean isAvailable;

    public BookableUnit(Washer washer, TimeSlot timeSlot) {
        this.washer = washer;
        this.timeSlot = timeSlot;
        this.isAvailable = true;
    }

    public BookableUnit() {

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
