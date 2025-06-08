package com.bcc.washer.service;

import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.repository.ReservationRepository;
import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasherService {

    @Autowired
    private WasherRepository washerRepository;


    //TODO: create washer - new bookable units available
    //TODO: delete washer - delete relating bookable units
    //TODO: status update is Available -> effect on bookableUnits

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
        existingWasher.setStatus(updatedWasher.getStatus());


        return washerRepository.save(existingWasher);
    }

    public void deleteWasher(Long id) {
        if (!washerRepository.existsById(id)) {
            throw new RuntimeException("Washer not found with ID: " + id);
        }
        // TODO: Consider handling active reservations for this washer before deletion.
        washerRepository.deleteById(id);
    }


}