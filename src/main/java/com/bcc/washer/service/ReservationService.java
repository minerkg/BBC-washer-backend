// src/main/java/com/bcc/washer/service/ReservationService.java
package com.bcc.washer.service;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.reservation.Reservation;
import com.bcc.washer.domain.reservation.ReservationStatus;
import com.bcc.washer.domain.time.TimeInterval;
import com.bcc.washer.domain.time.TimeSlot;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.dto.TimeSlotOptionDTO;
import com.bcc.washer.exceptions.BookableUnitNotAvailableException;
import com.bcc.washer.exceptions.ReservationNotFoundException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WasherRepository washerRepository;
    @Autowired
    private TimeIntervalRepository timeIntervalRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;


    private static final List<TimeSlotOptionDTO> AVAILABLE_TIME_SLOTS = Arrays.asList(
            new TimeSlotOptionDTO(LocalTime.of(8, 0), LocalTime.of(9, 0)),
            new TimeSlotOptionDTO(LocalTime.of(9, 0), LocalTime.of(10, 0)),
            new TimeSlotOptionDTO(LocalTime.of(10, 0), LocalTime.of(11, 0)),
            new TimeSlotOptionDTO(LocalTime.of(11, 0), LocalTime.of(12, 0)),
            new TimeSlotOptionDTO(LocalTime.of(12, 0), LocalTime.of(13, 0)),
            new TimeSlotOptionDTO(LocalTime.of(13, 0), LocalTime.of(14, 0)),
            new TimeSlotOptionDTO(LocalTime.of(14, 0), LocalTime.of(15, 0)),
            new TimeSlotOptionDTO(LocalTime.of(15, 0), LocalTime.of(16, 0)),
            new TimeSlotOptionDTO(LocalTime.of(16, 0), LocalTime.of(17, 0)),
            new TimeSlotOptionDTO(LocalTime.of(17, 0), LocalTime.of(18, 0)),
            new TimeSlotOptionDTO(LocalTime.of(18, 0), LocalTime.of(19, 0)),
            new TimeSlotOptionDTO(LocalTime.of(19, 0), LocalTime.of(20, 0)),
            new TimeSlotOptionDTO(LocalTime.of(20, 0), LocalTime.of(21, 0)),
            new TimeSlotOptionDTO(LocalTime.of(21, 0), LocalTime.of(22, 0))
    );

    public List<TimeSlotOptionDTO> getAvailableTimeSlots(Long washerId, LocalDate date) {

        List<BookableUnit> bookedUnits = bookableUnitRepository.findByWasherIdAndDate(washerId, date);
        Set<String> bookedTimes = bookedUnits.stream()
                .filter(bu -> !bu.isAvailable())
                .map(bu -> {
                    TimeInterval ti = bu.getTimeSlot().getTimeInterval();
                    return ti.getStartTime().toString() + "-" + ti.getEndTime().toString();
                })
                .collect(Collectors.toSet());

        return AVAILABLE_TIME_SLOTS.stream()
                .filter(slot -> !bookedTimes.contains(slot.getStartTime().toString() + "-" + slot.getEndTime().toString()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createReservation(Long washerId, LocalDate date, LocalTime startTime, LocalTime endTime, User user) {

        // Validate washer
        Washer washer = washerRepository.findById(washerId)
                .orElseThrow(() -> new IllegalArgumentException("Washer not found"));

        // Check if slot is available
        List<BookableUnit> existingUnits = bookableUnitRepository.findByWasherIdAndDate(washerId, date);
        boolean isBooked = existingUnits.stream().anyMatch(bu -> {
            if (bu.getTimeSlot().getTimeInterval()!=null){
                TimeInterval ti = bu.getTimeSlot().getTimeInterval();
                return ti.getStartTime().equals(startTime) && ti.getEndTime().equals(endTime);
            }
            return true;

        });
        if (isBooked) {
            throw new IllegalStateException("Time slot is already booked");
        }

        // Create TimeInterval
        TimeInterval timeInterval = timeIntervalRepository.findByStartTimeAndEndTime(startTime,endTime);
        System.out.println(timeInterval);
//
//        timeInterval.setDate(date);
//        timeInterval.setStartTime(startTime);
//        timeInterval.setEndTime(endTime);
//        timeInterval = timeIntervalRepository.save(timeInterval);

        // Create TimeSlot
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setTimeInterval(timeInterval);
        timeSlot = timeSlotRepository.save(timeSlot);

        // Create BookableUnit
        BookableUnit bookableUnit = new BookableUnit();
        bookableUnit.setWasher(washer);
        bookableUnit.setTimeSlot(timeSlot);
        bookableUnit = bookableUnitRepository.save(bookableUnit);

        // Create Reservation
        Reservation reservation = new Reservation();
        reservation.setBookableUnit(bookableUnit);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }




    public Set<Reservation> findAllByUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Filter for CONFIRMED bookings for the user, or all if you want history including cancelled
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getUser().getId() == userId)
                .filter(reservation -> reservation.getStatus() != ReservationStatus.CANCELLED) // Only show non-cancelled for user
                .collect(Collectors.toSet());
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));

        // Check if it's already cancelled
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new WasherStoreException("Reservation with ID " + reservationId + " is already cancelled.");
        }

        BookableUnit bookableUnit = reservation.getBookableUnit();
        if (bookableUnit == null) {

            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
            return;
        }

        TimeSlot timeSlot = bookableUnit.getTimeSlot();


        // Update bookable unit
        bookableUnit.setTimeSlot(null);
        bookableUnit.setAvailable(true);
        bookableUnitRepository.save(bookableUnit);

        // Update reservation status
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        if (timeSlot != null) {
            TimeInterval interval = timeSlot.getTimeInterval();

            if (interval != null) {
                interval.setTimeSlot(null); // 🔥 Rupe relația inversă
                timeSlot.setTimeInterval(null); // 🔥 Rupe relația directă
                timeIntervalRepository.save(interval); // 🔥 Salvează modificarea
            }
            timeSlotRepository.delete(timeSlot); // 🔥 Acum se poate șterge
        }
    }

    // New method for admin to view all reservations
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}