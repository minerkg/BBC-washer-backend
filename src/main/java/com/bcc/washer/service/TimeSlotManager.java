package com.bcc.washer.service;

import domain.TimeSlot;
import repository.TimeSlotRepository;

import java.util.List;

public class TimeSlotManager {

    private TimeSlotRepository timeSlotRepository;

    public TimeSlotManager(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }


    public List<TimeSlot> getAvailableTimeSlots() {
        //filter calendars
        return timeSlotRepository.getAll();
    }


}
