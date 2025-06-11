// src/main/java/com/bcc/washer/controller/ReservationController.java
package com.bcc.washer.controller;

import com.bcc.washer.domain.reservation.Reservation;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.dto.TimeSlotOptionDTO;
import com.bcc.washer.exceptions.BookableUnitNotAvailableException;
import com.bcc.washer.exceptions.ReservationNotFoundException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.exceptions.WasherStoreException; // Import if not already
import com.bcc.washer.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; // Changed from Set to List for findAllReservations
import java.util.Set;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Set<Reservation>>> getAllReservationByUser(Authentication authentication) {
        try {
            var user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(new ApiResponse<>("All active reservations of the client", reservationService.findAllByUser(user.getId())));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("User not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Internal server error", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> makeReservation(
            @RequestParam("washerId") Long washerId,
            @RequestParam("date") LocalDate date,
            @RequestParam("startTime") LocalTime startTime,
            @RequestParam("endTime") LocalTime endTime,
            Authentication authentication) {
        try {

            User user =(User) authentication.getPrincipal(); //
            reservationService.createReservation(washerId, date, startTime, endTime, user);
            return ResponseEntity.ok(new ApiResponse<>("Reservation created successfully", null));
        } catch (Exception e) {
                e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse<List<Reservation>>> getAllReservationsForAdmin() {
        try {
            List<Reservation> allReservations = reservationService.findAllReservations();
            return ResponseEntity.ok(new ApiResponse<>("All reservations retrieved", allReservations));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve all reservations", null));
        }
    }

    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse<List<TimeSlotOptionDTO>>> getAvailableTimeSlots(
            @RequestParam("washerId") Long washerId,
            @RequestParam("date") LocalDate date) {
        try {
            List<TimeSlotOptionDTO> slots = reservationService.getAvailableTimeSlots(washerId, date);
            return ResponseEntity.ok(new ApiResponse<>("Available time slots", slots));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

}