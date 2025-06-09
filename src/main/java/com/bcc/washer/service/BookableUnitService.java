package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeSlotManager timeSlotManager;


    // TODO: pageable needed
    public Set<BookableUnit> getAllAvailableBookableUnits() {
        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());

    }

    // TODO: for the next week
    public Set<BookableUnit> getAllAvailableBookableUnitsWithinOneWeek() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);
        return bookableUnitRepository.findAll()
                .stream()
                .filter(bu -> bu.isAvailable() && bu.getTimeSlot().getTimeInterval().getDate().isBefore(oneWeekLater))
                .collect(Collectors.toSet());

    }


    public void generateBookableUnits() {

        List<BookableUnit> newlyAvailableBookableUnits = new ArrayList<>();

        timeSlotRepository.findAllWithoutBookableUnits()
                .forEach(timeSlot ->
                        washerRepository.findAll().stream().filter(washer -> washer.getStatus().equals(WasherStatus.AVAILABLE))
                                .forEach(washer -> newlyAvailableBookableUnits.add(
                                        BookableUnit.builder()
                                                .isAvailable(true)
                                                .timeSlot(timeSlot)
                                                .washer(washer)
                                                .build())
                                )

                );
        bookableUnitRepository.saveAll(newlyAvailableBookableUnits);

    }


}
