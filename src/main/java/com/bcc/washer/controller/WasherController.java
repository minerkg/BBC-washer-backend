
package com.bcc.washer.controller;

import com.bcc.washer.domain.TemplateTYPE;
import com.bcc.washer.domain.washer.Washer;
<<<<<<< HEAD
import com.bcc.washer.service.EmailServiceImpl;
import com.bcc.washer.service.NotificationServiceI;
=======
import com.bcc.washer.domain.washer.WasherStatus;
>>>>>>> a32c5d3 (washer update effects added)
import com.bcc.washer.service.WasherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin/washers")
public class WasherController {

    @Autowired
    private WasherService washerService;
    @Autowired
    @Qualifier("emailNotificationService")
    private NotificationServiceI notificationServiceI;

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


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Washer>> getWasherById(@PathVariable Long id) {
        try {
            Washer washer = washerService.getWasherById(id);
            return ResponseEntity.ok(new ApiResponse<>("Washer retrieved", washer));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>("Failed to retrieve washer", null));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Washer>> updateWasher(@PathVariable Long id, @RequestBody Washer washer) {
        try {
            Washer updatedWasher = washerService.updateWasher(id, washer);
            return ResponseEntity.ok(new ApiResponse<>("Washer updated successfully", updatedWasher));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
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

    @PutMapping("/status-update/{id}")
    public ResponseEntity<ApiResponse<Washer>> updateWasherStatus(
            @PathVariable Long id,
            @RequestParam("newStatus") WasherStatus newStatus) {
        try {
            Washer updatedWasher = washerService.updateWasherStatus(id, newStatus);
            return ResponseEntity.ok(new ApiResponse<>("The washer's status successfully updated", updatedWasher));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Washer not found", null));
        }
    }

}