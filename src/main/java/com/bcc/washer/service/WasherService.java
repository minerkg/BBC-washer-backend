// src/main/java/com/bcc/washer/service/WasherService.java
package com.bcc.washer.service;

import com.bcc.washer.domain.ReservationStatus; // Import ReservationStatus
import com.bcc.washer.domain.TimeInterval; // Import TimeInterval
import com.bcc.washer.domain.Washer;
import com.bcc.washer.domain.WasherStatus; // Import the new enum
import com.bcc.washer.repository.WasherRepository;
import com.bcc.washer.repository.ReservationRepository; // Import ReservationRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WasherService {

    @Autowired
    private WasherRepository washerRepository;

    @Autowired
    private ReservationRepository reservationRepository; // Inject ReservationRepository

    public Washer addWasher(Washer washer) {
        // Set default status to AVAILABLE when a new washer is added
        if (washer.getStatus() == null) {
            washer.setStatus(WasherStatus.AVAILABLE);
        }
        return washerRepository.save(washer);
    }

    public List<Washer> getAllWashers() {
        return washerRepository.findAll();
    }

    public Washer getWasherById(Long id) {
        return washerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Washer not found with ID: " + id));
    }

    public Washer updateWasher(Long id, Washer updatedWasher) {
        Washer existingWasher = washerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Washer not found with ID: " + id));

        existingWasher.setName(updatedWasher.getName());
        existingWasher.setCapacity(updatedWasher.getCapacity());
        // Allow updating status only to AVAILABLE or MAINTENANCE via API.
        // IN_USE is determined dynamically.
        if (updatedWasher.getStatus() != WasherStatus.IN_USE) {
            existingWasher.setStatus(updatedWasher.getStatus());
        }


        return washerRepository.save(existingWasher);
    }

    public void deleteWasher(Long id) {
        if (!washerRepository.existsById(id)) {
            throw new RuntimeException("Washer not found with ID: " + id);
        }
        // TODO: Consider handling active reservations for this washer before deletion.
        washerRepository.deleteById(id);
    }

    /**
     * Determines the real-time status of all washers, considering current bookings.
     * Overrides the stored status with IN_USE if applicable.
     * @return List of Washers with their current real-time status.
     */
    public List<Washer> getAllWashersWithRealtimeStatus() {
        List<Washer> allWashers = washerRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        // Get all confirmed reservations that are currently active
        // Using EntityGraph to fetch TimeInterval and Washer data efficiently
        List<Long> washersInUseIds = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED)
                .filter(reservation -> {
                    TimeInterval interval = reservation.getBookableUnit().getTimeSlot().getTimeInterval();
                    return now.isAfter(interval.getStartTime()) && now.isBefore(interval.getEndTime());
                })
                .map(reservation -> reservation.getBookableUnit().getWasher().getId())
                .collect(Collectors.toList());

        // Update the status of each washer based on real-time usage
        return allWashers.stream().map(washer -> {
            if (washersInUseIds.contains(washer.getId())) {
                washer.setStatus(WasherStatus.IN_USE);
            }
            // If it's not IN_USE, its stored status (AVAILABLE or MAINTENANCE) remains.
            return washer;
        }).collect(Collectors.toList());
    }

    /**
     * Get the real-time status of a single washer by ID.
     * @param id The ID of the washer.
     * @return The Washer object with its current real-time status.
     */
    public Washer getWasherWithRealtimeStatusById(Long id) {
        Washer washer = getWasherById(id); // Throws RuntimeException if not found
        LocalDateTime now = LocalDateTime.now();

        boolean isInUse = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED)
                .filter(reservation -> reservation.getBookableUnit().getWasher().getId() == id)
                .anyMatch(reservation -> {
                    TimeInterval interval = reservation.getBookableUnit().getTimeSlot().getTimeInterval();
                    return now.isAfter(interval.getStartTime()) && now.isBefore(interval.getEndTime());
                });

        if (isInUse) {
            washer.setStatus(WasherStatus.IN_USE);
        }
        // If not in use, its stored status (AVAILABLE or MAINTENANCE) remains.
        return washer;
    }
}