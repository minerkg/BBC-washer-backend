// src/main/java/com/bcc/washer/controller/ReservationController.java
package com.bcc.washer.controller;

import com.bcc.washer.domain.Reservation;
import com.bcc.washer.exceptions.BookableUnitNotAvailableException;
import com.bcc.washer.exceptions.ReservationNotFoundException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.exceptions.WasherStoreException; // Import if not already
import com.bcc.washer.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Changed from Set to List for findAllReservations
import java.util.Set;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Set<Reservation>>> getAllReservationByUser(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("All active reservations of the client", reservationService.findAllByUser(userId)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("User not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Internal server error", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Reservation>> makeReservation(@RequestParam Long bookableUnitId, @RequestParam Long userId) {
        try {
            Reservation newReservation = reservationService.makeReservation(bookableUnitId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Appointment booked successfully", newReservation));
        } catch (BookableUnitNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>("Booking conflict", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("User not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Internal server error", null));
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<String>> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return ResponseEntity.ok(new ApiResponse<>("Reservation cancelled", "Reservation with ID " + reservationId + " has been successfully cancelled."));
        } catch (ReservationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Cancellation failed", e.getMessage()));
        } catch (WasherStoreException e) { // Catch the new exception for already cancelled
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>("Cancellation failed", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Internal server error", e.getMessage()));
        }
    }

    // New endpoint for ADMIN to view all reservations
    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse<List<Reservation>>> getAllReservationsForAdmin() {
        try {
            List<Reservation> allReservations = reservationService.findAllReservations();
            return ResponseEntity.ok(new ApiResponse<>("All reservations retrieved", allReservations));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve all reservations", null));
        }
    }
}