package com.bcc.washer.controller;

import com.bcc.washer.domain.Reservation;
import com.bcc.washer.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/reservation")
public class ReservationController {


    @Autowired
    private ReservationService reservationService;


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Set<Reservation>>> getAllReservationByUser(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(new ApiResponse<>("", reservationService.findAllByUser(userId)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }


    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Reservation>> makeReservation(@RequestParam Long bookableUnitId, @RequestParam Long userId) {
        try {
            Reservation newReservation = reservationService.makeReservation(bookableUnitId, userId);
            return ResponseEntity.ok(new ApiResponse<>("appointment booked", newReservation));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
