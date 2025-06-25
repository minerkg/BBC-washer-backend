package com.bcc.washer.service;

import com.bcc.washer.domain.ResourceNotExists;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.domain.washer.WasherStatus;
import com.bcc.washer.dto.WasherRequest;
import com.bcc.washer.exceptions.WasherAlreadyExistsException;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.repository.WasherRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(WasherService.class);


    public Washer addWasher(WasherRequest washer) {
        if (washer.getStatus() == null) {
            washer.setStatus(WasherStatus.AVAILABLE);
        }
        var savedWasher = washerRepository.save(
                Washer.builder()
                        .name(washer.getName())
                        .status(washer.getStatus())
                        .capacity(washer.getCapacity())
                        .build());
        bookableUnitService.updateBookableUnitsAfterWasherChange(savedWasher, "ADD");
        return savedWasher;
    }


    public List<Washer> getAllWashers() {
        return washerRepository.findAll();
    }

    public Washer getWasherById(Long id) {
        return washerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Washer not found with ID: " + id));
    }

    @Transactional
    public Washer updateWasher(Long id, Washer updatedWasher) {
        Washer existingWasher = washerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Washer not found with ID: " + id));

        existingWasher.setName(updatedWasher.getName());
        existingWasher.setCapacity(updatedWasher.getCapacity());

        if (!existingWasher.getStatus().equals(updatedWasher.getStatus())) {
            updateWasherStatus(existingWasher.getId(), updatedWasher.getStatus());
        }

        return existingWasher;
    }

    public void deleteWasher(Long id) {
        Washer washer = washerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotExists("Washer not found with ID: " + id));
        bookableUnitService.updateBookableUnitsAfterWasherChange(washer, "DELETE");
        washer.setStatus(WasherStatus.DECOMMISSIONED);
        washerRepository.save(washer);
        //washerRepository.deleteById(id);
    }


    public void verifyDuplicateWasher(WasherRequest washer) {
        Optional<Washer> existing = washerRepository.findByName(washer.getName());
        if (existing.isPresent()) {
            throw new WasherAlreadyExistsException("Washer with name: " + washer.getName() + " already exists");
        }
    }

    @Transactional
    public Washer updateWasherStatus(Long id, WasherStatus newStatus) {
        Washer existingWasher = washerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotExists("Washer not found with ID: " + id));
        try {
            existingWasher.setStatus(newStatus);
            bookableUnitService.updateBookableUnitsAfterWasherChange(existingWasher, "STATUS-UPDATE");
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new WasherStoreException("Impossible to update the washer's status: ");
        }

        return existingWasher;
    }


}