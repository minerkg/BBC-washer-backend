package com.bcc.washer.service;


import com.bcc.washer.domain.OPENINGHOURS;
import com.bcc.washer.domain.ResourceAlreadyExistsException;
import com.bcc.washer.domain.time.TimeInterval;
import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TimeSlotManager {


    @Autowired
    private TimeSlotRepository timeSlotRepository;


    public Set<TimeSlot> getAvailableTimeSlotsBetweenDates(LocalDate timeSlotStartDayFrom, LocalDate to) {
        return new HashSet<>(timeSlotRepository.findAllByDateRange(timeSlotStartDayFrom, to));
    }


    public List<TimeSlot> createTimeSlots(LocalDate timeSlotStartDayFrom, LocalDate to) {

        final LocalDate from = timeSlotStartDayFrom;

        TimeSlot latestExistingTimeSlot = getLatestExistingTimeSlot()
                .orElseGet(() -> createFallbackSlot(from));

        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(to) || latestExistingTimeSlot.getTimeInterval().getDate().isEqual(to)) {
            throw new ResourceAlreadyExistsException("TimeSlots are already available");
        }
        if (latestExistingTimeSlot.getTimeInterval().getDate().isAfter(timeSlotStartDayFrom)) {
            timeSlotStartDayFrom = latestExistingTimeSlot.getTimeInterval().getDate().plusDays(1);
        }

        //generating TimeSlots
        List<TimeSlot> newlyAvailableTimeSlots = new ArrayList<>();

        for (LocalDate date = timeSlotStartDayFrom; !date.isAfter(to); date = date.plusDays(1)) {
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
                        .bookableUnit(new ArrayList<>())
                        .build();
                newlyAvailableTimeSlots.add(newTimeSlot);
            }
        }
        timeSlotRepository.saveAll(newlyAvailableTimeSlots);

        return newlyAvailableTimeSlots;
    }

    private Optional<TimeSlot> getLatestExistingTimeSlot() {
        return timeSlotRepository.findAll().stream()
                .max(Comparator.comparing(ts -> ts.getTimeInterval().getDate()));

    }

    private TimeSlot createFallbackSlot(LocalDate from) {
        return TimeSlot.builder()
                .timeInterval(TimeInterval.builder()
                        .startTime(OPENINGHOURS.CLOSE.getTime().minusHours(1))
                        .endTime(OPENINGHOURS.CLOSE.getTime())
                        .date(from.minusDays(1))
                        .build())
                .build();
    }


    //TODO: isn't working - time to fix it
    public void deleteTimeSlot(Long id) {
        timeSlotRepository.deleteById(id);
    }
}
