package com.bcc.washer.service;


import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class TimeSlotManager {


    @Autowired
    private TimeSlotRepository timeSlotRepository;


    public Set<TimeSlot> getAvailableTimeSlotsBetweenDates(LocalDate timeSlotStartDayFrom, LocalDate to) {
        return new HashSet<>(timeSlotRepository.findAllByDateRange(timeSlotStartDayFrom, to));
    }

    //TODO: isn't working - time to fix it
    public void deleteTimeSlot(Long id) {
        timeSlotRepository.deleteById(id);
    }
}
