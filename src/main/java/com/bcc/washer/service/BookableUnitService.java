package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private ReservationService reservationService;

    @Autowired
    @Qualifier("emailNotificationService")
    private NotificationServiceI notificationService;


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
                    generateNewBookableUnitsAfterWasherAdd(washer, newlyAvailableBookableUnits);
                    bookableUnitRepository.saveAll(newlyAvailableBookableUnits);
                    return "Bookable unit add finished after washer add";
                });
            }
            case "DELETE" -> CompletableFuture.supplyAsync(() -> {
                List<BookableUnit> deletableUnits = new ArrayList<>();
                List<BookableUnit> futureUnits = bookableUnitRepository.findAllByWasherAfterNow(washer.getId(), LocalDateTime.now());
                for (BookableUnit bu : futureUnits) {
                    if (!bu.isAvailable()) {
                        boolean rescheduled = tryReschedule(bu);
                        if (!rescheduled) {
                            notifyUserOfCancellation(bu);
                        }
                    } else {
                        deletableUnits.add(bu);
                    }
                }
                bookableUnitRepository.deleteAll(deletableUnits);
                return "Reservations rescheduled and customers notified";
            });

            case "STATUS-UPDATE" -> {
                if (washer.getStatus() == WasherStatus.AVAILABLE) {
                    List<BookableUnit> newlyAvailableBUs = new ArrayList<>();
                    CompletableFuture.supplyAsync(() -> {
                        generateNewBookableUnitsAfterWasherAdd(washer, newlyAvailableBUs);
                        bookableUnitRepository.saveAll(newlyAvailableBUs);
                        return "Bookable unit add finished after washer add";
                    });
                }
                if (washer.getStatus() == WasherStatus.MAINTENANCE) {
                    CompletableFuture.supplyAsync(() -> {
                        List<BookableUnit> deletableUnits = new ArrayList<>();
                        List<BookableUnit> futureUnits = bookableUnitRepository.findAllByWasherAfterNow(washer.getId(), LocalDateTime.now());
                        for (BookableUnit bu : futureUnits) {
                            if (!bu.isAvailable()) {
                                boolean rescheduled = tryReschedule(bu);
                                if (!rescheduled) {
                                    notifyUserOfCancellation(bu);
                                }
                            } else {
                                deletableUnits.add(bu);
                            }
                        }
                        bookableUnitRepository.deleteAll(deletableUnits);
                        return "Reservations rescheduled and customers notified";
                    });
                }
            }
            default -> throw new WasherStoreException("BookableUnit update error after washer change");


        }
        return CompletableFuture.supplyAsync(() -> "");
    }

    private void generateNewBookableUnitsAfterWasherAdd(Washer washer, List<BookableUnit> newlyAvailableBookableUnits) {
        timeSlotRepository.findAll().stream()
                .filter(ts -> ts.getTimeInterval().getDate().isAfter(LocalDate.now()))
                .forEach(ts -> newlyAvailableBookableUnits.add(
                        BookableUnit.builder()
                                .isAvailable(true)
                                .timeSlot(ts)
                                .washer(washer)
                                .build()));
    }


    private boolean tryReschedule(BookableUnit bu) {
        return bookableUnitRepository.findAllByDate(bu.getTimeSlot().getTimeInterval().getDate()).stream()
                .filter(BookableUnit::isAvailable)
                .findFirst()
                .map(freeBu -> {
                    reservationService.makeReservation(freeBu.getId(), bu.getReservation().getUser().getId());
                    return true;
                }).orElse(false);
    }

    private void notifyUserOfCancellation(BookableUnit bu) {
        notificationService.notifyReservation(
                bu.getReservation().getUser().getEmail(),
                "Appointment deleted",
                "Dear user, due to a technical issue with the washer, your reservation cannot be rescheduled.",
                bu);
    }

}
