package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.TimeInterval;
import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.domain.Washer;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeIntervalRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private TimeIntervalRepository timeIntervalRepository;


    public Set<BookableUnit> getAllAvailableBookableUnits() {
        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());

    }

    public Set<BookableUnit> getAllABookableUnitsHistoryByUser(Long userId) {
        //// not working yet

        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void generateBookableUnits() {
        List<TimeSlot> futureTimeSlotList = new ArrayList<>();
        List<TimeInterval> futureTimeIntervals = timeIntervalRepository.findAll()
                .stream().filter(timeInterval -> timeInterval.getDate().isAfter(LocalDate.now()))
                .toList();
        futureTimeIntervals.forEach(ti -> futureTimeSlotList.add(TimeSlot.builder().timeInterval(ti).build()));

        washerRepository.findAll().stream().filter(Washer::isInOrder)
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
