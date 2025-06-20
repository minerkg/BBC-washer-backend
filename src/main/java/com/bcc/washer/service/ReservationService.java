// src/main/java/com/bcc/washer/service/ReservationService.java
package com.bcc.washer.service;

import com.bcc.washer.domain.BookableUnit;
import com.bcc.washer.domain.reservation.Reservation;
import com.bcc.washer.domain.reservation.ReservationStatus; // Import the new enum
import com.bcc.washer.repository.BookableUnitRepository;
import com.bcc.washer.repository.ReservationRepository;
import com.bcc.washer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; // Changed from Set to List for findAll (can be Set too, just consistency)
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.exceptions.BookableUnitNotAvailableException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.exceptions.ReservationNotFoundException;

@Service
public class ReservationService {

    @Autowired
    private BookableUnitRepository bookableUnitRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("emailNotificationService")
    private NotificationServiceI notificationServiceI;

    @Transactional
    public Reservation makeReservation(Long bookableUnitId, Long userId) {
        var bookableUnit = bookableUnitRepository.findById(bookableUnitId)
                .orElseThrow(() -> new WasherStoreException("Bookable Unit not found with ID: " + bookableUnitId));

        if (!bookableUnit.isAvailable()) {
            throw new BookableUnitNotAvailableException("Bookable Unit with ID: " + bookableUnitId + " is not available.");
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Mark the bookable unit as unavailable
        bookableUnit.setAvailable(false);
        bookableUnitRepository.save(bookableUnit); // Save the updated bookable unit

        Reservation newReservation = Reservation.builder()
                .user(user)
                .bookableUnit(bookableUnit)
                .status(ReservationStatus.CONFIRMED) // Set initial status
                .build();
        // TODO : Change for user.email !
//        CompletableFuture.runAsync(() -> notificationServiceI.notifyReservation(user.getEmail(),"Reservation Created: ","Washer has been updated: ",bookableUnit));

        CompletableFuture.runAsync(() -> notificationServiceI.notifyReservation("zblaziu@gmail.com","Reservation Created: "," ",bookableUnit));

        return reservationRepository.save(newReservation);
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
        if (bookableUnit != null) {
            bookableUnit.setAvailable(true); // Make the bookable unit available again
            bookableUnitRepository.save(bookableUnit);
        }
        reservation.setStatus(ReservationStatus.CANCELLED); // Set status to CANCELLED
        reservationRepository.save(reservation); // Save the updated reservation instead of deleting
    }

    // New method for admin to view all reservations
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}