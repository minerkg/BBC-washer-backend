package com.bcc.washer.controller;


import com.bcc.washer.domain.ResourceAlreadyExistsException;
import com.bcc.washer.domain.ResourceNotExists;
import com.bcc.washer.domain.TimeSlot;
import com.bcc.washer.dto.BetweenDatesRequest;
import com.bcc.washer.service.TimeSlotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/time-slot")
public class TimeSlotController {

    @Autowired
    private TimeSlotManager timeSlotManager;

    Logger logger = LoggerFactory.getLogger(TimeSlotController.class);


    @GetMapping("")
    public ResponseEntity<ApiResponse<Set<TimeSlot>>> getAllAvailableTimeSlotsBetweenDates(
            @RequestBody BetweenDatesRequest betweenDatesRequest) {
        try {
            logger.info("---  getAllTimeSlots method accessed  ---");
            return ResponseEntity.ok()
                    .body(new ApiResponse<>("all timeslots",
                                    timeSlotManager.getAvailableTimeSlotsBetweenDates(
                                            betweenDatesRequest.getStartDate(),
                                            betweenDatesRequest.getEndDate())
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<String>> generateTimeSlots(@RequestBody BetweenDatesRequest betweenDatesRequest) {
        try {
            timeSlotManager.createTimeSlots(
                    betweenDatesRequest.getStartDate(),
                    betweenDatesRequest.getEndDate());
            return ResponseEntity.ok().build();
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ApiResponse<>("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTimeSlot(@PathVariable("id") Long id) {
        try {
            timeSlotManager.deleteTimeSlot(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (ResourceNotExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
