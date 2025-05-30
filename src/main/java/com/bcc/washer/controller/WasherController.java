// src/main/java/com/bcc/washer/controller/WasherController.java
package com.bcc.washer.controller;

import com.bcc.washer.domain.Washer;
import com.bcc.washer.service.WasherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/washers")
public class WasherController {

    @Autowired
    private WasherService washerService;

    @PostMapping
    public ResponseEntity<ApiResponse<Washer>> addWasher(@RequestBody Washer washer) {
        try {
            Washer newWasher = washerService.addWasher(washer);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Washer added successfully", newWasher));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to add washer", null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Washer>>> getAllWashers() {
        try {
            // This will return the stored status (AVAILABLE/MAINTENANCE)
            List<Washer> washers = washerService.getAllWashers();
            return ResponseEntity.ok(new ApiResponse<>("All washers retrieved", washers));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve washers", null));
        }
    }

    // New endpoint to get all washers with real-time status (includes IN_USE)
    @GetMapping("/status") // Can be accessed by EMPLOYEE or USER roles as well
    public ResponseEntity<ApiResponse<List<Washer>>> getAllWashersWithRealtimeStatus() {
        try {
            List<Washer> washers = washerService.getAllWashersWithRealtimeStatus();
            return ResponseEntity.ok(new ApiResponse<>("All washers with real-time status retrieved", washers));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve real-time washer statuses", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Washer>> getWasherById(@PathVariable Long id) {
        try {
            // This will return the stored status (AVAILABLE/MAINTENANCE)
            Washer washer = washerService.getWasherById(id);
            return ResponseEntity.ok(new ApiResponse<>("Washer retrieved", washer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve washer", null));
        }
    }

    // New endpoint to get a single washer with real-time status
    @GetMapping("/{id}/status") // Can be accessed by EMPLOYEE or USER roles as well
    public ResponseEntity<ApiResponse<Washer>> getWasherWithRealtimeStatusById(@PathVariable Long id) {
        try {
            Washer washer = washerService.getWasherWithRealtimeStatusById(id);
            return ResponseEntity.ok(new ApiResponse<>("Washer with real-time status retrieved", washer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve real-time washer status", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Washer>> updateWasher(@PathVariable Long id, @RequestBody Washer washer) {
        try {
            Washer updatedWasher = washerService.updateWasher(id, washer);
            return ResponseEntity.ok(new ApiResponse<>("Washer updated successfully", updatedWasher));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to update washer", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteWasher(@PathVariable Long id) {
        try {
            washerService.deleteWasher(id);
            return ResponseEntity.ok(new ApiResponse<>("Washer deleted", "Washer with ID " + id + " has been successfully deleted."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to delete washer", e.getMessage()));
        }
    }
}