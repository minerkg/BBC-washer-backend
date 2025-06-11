package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.dto.BookableUnitDTO;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private ReservationService reservationService;


    // TODO: pageable needed
    @Transactional
    public Set<BookableUnit> getAllAvailableBookableUnits() {
        return bookableUnitRepository.findAll()
                .stream()
                .filter(BookableUnit::isAvailable)
                .collect(Collectors.toSet());

    }

    // TODO: for the next week
    @Transactional
    public List<BookableUnitDTO> getAllAvailableBookableUnitsWithinOneWeek() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);
        return bookableUnitRepository.findAllAvailableWithinDateRange(today, oneWeekLater)
                .stream()
                .filter(BookableUnit::isAvailable)
                .map(bu -> new BookableUnitDTO(
                        bu.getId(),
                        bu.getWasher().getId(),
                        bu.getWasher().getName(),
                        bu.getWasher().getCapacity(),
                        bu.getWasher().getStatus(),
                        bu.getTimeSlot().getId(),
                        bu.getTimeSlot().getTimeInterval().getDate(),
                        bu.getTimeSlot().getTimeInterval().getStartTime(),
                        bu.getTimeSlot().getTimeInterval().getEndTime(),
                        true
                ))
                .collect(Collectors.toList());
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
