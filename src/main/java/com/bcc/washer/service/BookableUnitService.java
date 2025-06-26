package com.bcc.washer.service;


import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.reservation.ReservationStatus;
import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.WasherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Autowired
    @Qualifier("emailNotificationService")
    private NotificationServiceI notificationService;

    private Logger logger = LoggerFactory.getLogger(BookableUnitService.class);

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


    @Transactional
    public void generateBookableUnits() {

        List<BookableUnit> newlyAvailableBookableUnits = new ArrayList<>();

        timeSlotRepository.findAllWithoutBookableUnits()
                .forEach(timeSlot -> {
                    TimeSlot attachedTimeSlot = timeSlotRepository.findById(timeSlot.getId())
                            .orElseThrow(() -> new IllegalStateException("TimeSlot with ID " + timeSlot.getId() + " not found"));

                    washerRepository.findAll().stream().filter(washer -> washer.getStatus().equals(WasherStatus.AVAILABLE))
                            .forEach(washer -> newlyAvailableBookableUnits.add(
                                    BookableUnit.builder()
                                            .isAvailable(true)
                                            .timeSlot(attachedTimeSlot)
                                            .washer(washer)
                                            .build())
                            );

                });
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

    public void updateBookableUnitsAfterWasherChange(Washer washer, String payload) {
        switch (payload) {
            case "ADD" -> {
                logger.info("updateBookableUnitsAfterWasherChange - ADD accessed");
                if (washer.getStatus().equals(WasherStatus.AVAILABLE)) {
                    logger.info("updateBookableUnitsAfterWasherChange - ADD - if washer available accessed");
                    List<BookableUnit> newlyAvailableBookableUnits = new ArrayList<>();
                    generateNewBookableUnitsAfterWasherAdd(washer, newlyAvailableBookableUnits);
                    bookableUnitRepository.saveAll(newlyAvailableBookableUnits);
                }
            }
            case "DECOMMISSION" -> {
                logger.info("updateBookableUnitsAfterWasherChange - DECOMMISSION accessed");
                setUnavailableBookableUnitsAndCancelReservations(washer);
            }

            case "STATUS-UPDATE" -> {
                if (washer.getStatus().equals(WasherStatus.AVAILABLE)) {
                    List<BookableUnit> newlyAvailableBUs = new ArrayList<>();
                    generateNewBookableUnitsAfterWasherAdd(washer, newlyAvailableBUs);
                    bookableUnitRepository.saveAll(newlyAvailableBUs);
                }
                try {
                    if (washer.getStatus().equals(WasherStatus.MAINTENANCE)) {
                        setUnavailableBookableUnitsAndCancelReservations(washer);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new WasherStoreException(e.getMessage());
                }
            }
            default -> throw new WasherStoreException("BookableUnit update error after washer change");
        }

    }

    //@Transactional
    protected void setUnavailableBookableUnitsAndCancelReservations(Washer washer) {
        List<BookableUnit> unAvailableUnits = new ArrayList<>();
        List<BookableUnit> futureUnits = bookableUnitRepository
                .findAllByWasherAfterNow(washer.getId(), LocalDate.now()
                        .plusDays(1));
        for (BookableUnit bu : futureUnits) {
            if (bu.getReservation() != null) {
                logger.info("setUnavailableBookableUnitsAndCancelReservations accessed --- " + bu);
                boolean rescheduled = tryReschedule(bu);
                if (!rescheduled) {
                    notifyUserOfCancellation(bu);
                }
                //cancel the reservation anyway
                bu.getReservation().setStatus(ReservationStatus.CANCELLED);
            }
            bu.setAvailable(false);
            unAvailableUnits.add(bu);
        }
        bookableUnitRepository.saveAll(unAvailableUnits);
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
        return bookableUnitRepository.findAllByTimeSlotId(bu.getTimeSlot().getId()).stream()
                .filter(BookableUnit::isAvailable)
                .findFirst()
                .map(freeBu -> {
                    reservationService.makeReservation(freeBu.getId(), bu.getReservation().getUser().getId());
                    return true;
                }).orElse(false);
    }

    private void notifyUserOfCancellation(BookableUnit bu) {
        logger.info("notifyUserOfCancellation accessed --- sending cancellation e-mail ");
        notificationService.notifyReservation(
                bu.getReservation().getUser().getEmail(),
                "Appointment deleted",
                "Dear user, due to a technical issue with the washer, your reservation cannot be rescheduled.",
                bu);
    }

}
