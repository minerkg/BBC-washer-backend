package com.bcc.washer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;


@Entity
public class TimeSlot {

    @Id
    private Long id;

    @OneToOne
    private TimeInterval timeInterval;

    @OneToMany(mappedBy = "timeSlot")
    private List<BookableUnit> bookableUnit;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public List<BookableUnit> getBookableUnit() {
        return bookableUnit;
    }

    public void setBookableUnit(List<BookableUnit> bookableUnit) {
        this.bookableUnit = bookableUnit;
    }
}
