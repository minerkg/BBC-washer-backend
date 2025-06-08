package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.OPENINGHOURS;
import com.bcc.washer.domain.TimeInterval;
import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Transactional
    public List<TimeSlot> createTimeSlots(LocalDate from, LocalDate to, List<BookableUnit> bookableUnits) {
        LocalDate newTimeSlotStartDay = from.plusDays(1);

        TimeSlot latestExistingTimeSlot = timeSlotRepository.findAll().stream()
                .max(Comparator.comparing(ts -> ts.getTimeInterval().getEndTime()))
                .orElse(TimeSlot
                        .builder()
                        .timeInterval(TimeInterval
                                .builder()
                                .startTime(OPENINGHOURS.CLOSE.getTime().minusHours(1))
                                .endTime(OPENINGHOURS.CLOSE.getTime())
                                .date(from)
                                .build())
                        .build());
        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(to)) {
            throw new RuntimeException("TimeSlots are already available");
        }
        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(from)) {
            newTimeSlotStartDay = latestExistingTimeSlot.getTimeInterval().getDate().plusDays(1);
        }

        //generating TimeSlots
        List<TimeSlot> newlyAvailableTimeSlots = new ArrayList<>();

        for (LocalDate date = newTimeSlotStartDay; !date.isAfter(to); date = date.plusDays(1)) {
            for (LocalTime timeIntervalStart = OPENINGHOURS.OPEN.getTime();
                 timeIntervalStart.isBefore(OPENINGHOURS.CLOSE.getTime());
                 timeIntervalStart = timeIntervalStart.plusHours(1)) {
                TimeSlot newTimeSlot = TimeSlot
                        .builder()
                        .timeInterval(TimeInterval
                                .builder()
                                .startTime(timeIntervalStart)
                                .endTime(timeIntervalStart.plusHours(1))
                                .date(date)
                                .build())
                        .bookableUnit(bookableUnits)
                        .build();
                newlyAvailableTimeSlots.add(timeSlotRepository.save(newTimeSlot));
            }
        }
        return newlyAvailableTimeSlots;
    }


}
