package com.bcc.washer.service;

import com.bcc.washer.domain.ResourceNotExists;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;

import com.bcc.washer.exceptions.WasherAlreadyExistsException;
import com.bcc.washer.repository.ReservationRepository;

import com.bcc.washer.exceptions.WasherStoreException;

import com.bcc.washer.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WasherService {

    @Autowired
    private WasherRepository washerRepository;

    @Autowired
    private BookableUnitService bookableUnitService;


    public Washer addWasher(Washer washer) {
        if (washer.getStatus() == null) {
            washer.setStatus(WasherStatus.AVAILABLE);
        }
        bookableUnitService.updateBookableUnitsAfterWasherChange(washer, "ADD");
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

        if (!existingWasher.getStatus().equals(updatedWasher.getStatus())) {
            updateWasherStatus(existingWasher.getId(), updatedWasher.getStatus());
        }

        return washerRepository.save(existingWasher);
    }

    public void deleteWasher(Long id) {
        Washer washer = washerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotExists("Washer not found with ID: " + id));
        bookableUnitService.updateBookableUnitsAfterWasherChange(washer, "DELETE");
        washerRepository.deleteById(id);
    }


    public void verifyDuplicateWasher(Washer washer) {
        Optional<Washer> existing = washerRepository.findByName(washer.getName());
        if (existing.isPresent()) {
            throw new WasherAlreadyExistsException("Washer with name: " + washer.getName() + " already exists");
        }
    }


    public Washer updateWasherStatus(Long id, WasherStatus newStatus) {
        Washer existingWasher = washerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotExists("Washer not found with ID: " + id));
        try {
            existingWasher.setStatus(newStatus);
            bookableUnitService.updateBookableUnitsAfterWasherChange(existingWasher, "STATUS-UPDATE");
        } catch (RuntimeException e) {
            throw new WasherStoreException("Impossible to update the washer's status");
        }

        return washerRepository.save(existingWasher);
    }


}