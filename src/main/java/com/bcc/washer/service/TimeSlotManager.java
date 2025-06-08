package com.bcc.washer.service;


import com.bcc.washer.domain.OPENINGHOURS;
import com.bcc.washer.domain.TimeInterval;
import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class TimeSlotManager {


    @Autowired
    private TimeSlotRepository timeSlotRepository;



    public List<TimeSlot> getAvailableTimeSlots() {
        //filter calendars
        return timeSlotRepository.findAll();
    }

    public TimeSlot createTimeSlot(LocalDate from, LocalDate to) {
        LocalDate newTimeSlotStartDay = from.plusDays(1);

        TimeSlot latestExistingTimeSlot = timeSlotRepository.findAll().stream()
                .max(Comparator.comparing(ts -> ts.getTimeInterval().getEndTime()))
                .orElse(TimeSlot
                        .builder()
                        .timeInterval(TimeInterval
                                .builder()
                                .startTime(from.atTime(OPENINGHOURS.CLOSE.getTime().minusHours(1)))
                                .endTime(to.atTime(OPENINGHOURS.CLOSE.getTime()))
                                .date(from)
                                .build())
                        .build());
        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(to)) {
            throw new RuntimeException("TimeSlots are already available");
        }
        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(from)) {
            newTimeSlotStartDay = latestExistingTimeSlot.getTimeInterval().getDate().plusDays(1);
        }





        latestExistingTimeSlot
        //checks if the timeslots already exists
        //if yes than use that
        //else create new timeSlot



    }


}
