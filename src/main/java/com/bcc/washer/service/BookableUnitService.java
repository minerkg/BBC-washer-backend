// src/main/java/com/bcc/washer/service/BookableUnitService.java
package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.time.TimeInterval;
import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeIntervalRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookableUnitService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;


    @Autowired
    private WasherRepository washerRepository;


    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeIntervalRepository timeIntervalRepository;


    public Set<BookableUnit> getAllAvailableBookableUnits() {
        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());

    }

    public Set<BookableUnit> getAllAvailableBookableUnitsWithinOneWeek() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);
        return bookableUnitRepository.findAll()
                .stream()
                .filter(bu -> bu.isAvailable() && bu.getTimeSlot().getTimeInterval().getDate().isBefore(oneWeekLater))
                .collect(Collectors.toSet());

    }

    @Transactional
    public void generateBookableUnits() {
        List<TimeInterval> futureTimeIntervals = timeIntervalRepository.findAll()
                .stream().filter(timeInterval ->
                                timeInterval.getDate().isAfter(LocalDate.now())
                        /*&& timeInterval.getTimeSlot() != null*/)
                .toList();
        futureTimeIntervals.forEach(ti -> timeSlotRepository.save(TimeSlot.builder().timeInterval(ti).build()));

        var futureTimeSlotList = timeSlotRepository.findAll();

        // FIX: Changed Washer::isInOrder to check for WasherStatus.AVAILABLE
        washerRepository.findAll().stream()
                .filter(washer -> washer.getStatus() == WasherStatus.AVAILABLE) // Changed condition here
                .forEach(washer ->
                        futureTimeSlotList.forEach(timeSlot ->
                                bookableUnitRepository.save(
                                        BookableUnit.builder()
                                                .isAvailable(true)
                                                .timeSlot(timeSlot)
                                                .washer(washer)
                                                .build())

                        )
                );
    }


}