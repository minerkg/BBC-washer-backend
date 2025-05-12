package com.bcc.washer.service;


import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotManager {


    private final TimeSlotRepository timeSlotRepository;


    public TimeSlotManager(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }


    public List<TimeSlot> getAvailableTimeSlots() {
        //filter calendars
        return timeSlotRepository.findAll();
    }


}
