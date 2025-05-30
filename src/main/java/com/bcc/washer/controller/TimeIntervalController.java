// src/main/java/com/bcc/washer/controller/TimeIntervalController.java
package com.bcc.washer.controller;

import com.bcc.washer.domain.TimeInterval;
import com.bcc.washer.service.TimeManagementService; // Use the renamed service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/time-intervals") // Likely an admin function
public class TimeIntervalController {

    @Autowired
    private TimeManagementService timeManagementService; // Use the renamed service

    @PostMapping("/generate-daily")
    public ResponseEntity<ApiResponse<List<TimeInterval>>> generateDailyTimeIntervals(@RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date); // Expects YYYY-MM-DD format
            List<TimeInterval> intervals = timeManagementService.generateDailyTimeIntervals(localDate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Daily time intervals generated successfully", intervals));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to generate daily time intervals", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TimeInterval>> createTimeInterval(@RequestBody TimeInterval timeInterval) {
        try {
            TimeInterval newTimeInterval = timeManagementService.saveTimeInterval(timeInterval);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Time interval created successfully", newTimeInterval));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to create time interval", null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimeInterval>>> getAllTimeIntervals() {
        try {
            List<TimeInterval> intervals = timeManagementService.getAllTimeIntervals();
            return ResponseEntity.ok(new ApiResponse<>("All time intervals retrieved", intervals));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve time intervals", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TimeInterval>> getTimeIntervalById(@PathVariable Long id) {
        try {
            TimeInterval interval = timeManagementService.getTimeIntervalById(id);
            return ResponseEntity.ok(new ApiResponse<>("Time interval retrieved", interval));
        } catch (RuntimeException e) { // Assuming service throws RuntimeException for not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Time interval not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve time interval", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTimeInterval(@PathVariable Long id) {
        try {
            timeManagementService.deleteTimeInterval(id);
            return ResponseEntity.ok(new ApiResponse<>("Time interval deleted", "Time interval with ID " + id + " has been successfully deleted."));
        } catch (RuntimeException e) { // Assuming service throws RuntimeException for not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Deletion failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to delete time interval", e.getMessage()));
        }
    }
}