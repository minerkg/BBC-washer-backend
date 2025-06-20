package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
    public Set<BookableUnit> getAllBookableUnits() {
        return new HashSet<>(bookableUnitRepository.findAll());

    }

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

    public Set<BookableUnit> getAllAvailableBookableUnitsWithinOneWeek(LocalDate localDate) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);

        return new HashSet<>(bookableUnitRepository.findAllByDate(localDate)
                .stream()
                .filter(bu -> bu.isAvailable()
                        && bu.getTimeSlot().getTimeInterval().getDate().isBefore(oneWeekLater))
                .collect(Collectors.groupingBy(
                        bu -> bu.getTimeSlot().getTimeInterval().getStartTime(),
                        Collectors.collectingAndThen(Collectors.toList(), List::getFirst)
                ))
                .values());
    }

    public CompletableFuture<String> updateBookableUnitsAfterWasherChange(Washer washer, String payload) {
        switch (payload) {
            case "ADD" -> {
                List<BookableUnit> newlyAvailableBookableUnits = new ArrayList<>();
                CompletableFuture.supplyAsync(() -> {
                    timeSlotRepository.findAll().stream()
                            .filter(ts -> ts.getTimeInterval().getDate().isAfter(LocalDate.now()))
                            .forEach(ts -> newlyAvailableBookableUnits.add(
                                    BookableUnit.builder()
                                            .isAvailable(true)
                                            .timeSlot(ts)
                                            .washer(washer)
                                            .build()));
                    return "Bookable unit add finished after washer add";
                });
                //TODO: notify
            }
            case "DELETE" -> {

                //TODO: notify
            }
            case "STATUS-UPDATE" -> {

                //TODO: notify
            }
            default -> throw new WasherStoreException("BookableUnit update error after washer change");


        }
    }
}
