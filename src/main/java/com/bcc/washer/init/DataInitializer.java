package com.bcc.washer.init;

import com.bcc.washer.controller.TimeSlotController;
import com.bcc.washer.domain.ResourceAlreadyExistsException;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.domain.washer.Washer;
import com.bcc.washer.dto.WasherRequest;
import com.bcc.washer.repository.UserRepository;
import com.bcc.washer.repository.WasherRepository;
import com.bcc.washer.service.BookableUnitService;
import com.bcc.washer.service.TimeSlotManager;
import com.bcc.washer.service.WasherService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(TimeSlotController.class);

    private final UserRepository userRepository;

    private final TimeSlotManager timeSlotManager;

    private final WasherRepository washerRepository;
    private final WasherService washerService;


    @Override
    public void run(ApplicationArguments args) {
        initUsers();
        initTimeSlots();
        initWashers();

    }

    public void initUsers() {
        long existingUsers = userRepository.count();
        if (existingUsers > 0) {
            logger.warn("Skipping user initialization: {} users already exist", existingUsers);
            userRepository.findAll().forEach(user -> logger.info("Existing user: {}", user.getUsername()));
            return;
        } else {
            logger.info("Starting user initialization");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("default-users.json").getInputStream();

            List<User> users = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (User user : users) {
                userRepository.save(user);
                logger.info("Created user '{}'", user.getUsername());
            }

        } catch (Exception e) {
            logger.error("Failed to initialize users from JSON", e);
        }
    }

    private void initTimeSlots() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);

        try {
            timeSlotManager.createTimeSlots(startDate, endDate);
            logger.info("Time slots initialized from {} to {}", startDate, endDate);
        } catch (ResourceAlreadyExistsException e) {
            logger.warn("Skipping slot initialization: time slots already exist for period {} - {}, {}", startDate, endDate, e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to initialize time slots", e);
        }
    }

    private void initWashers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("default-washers.json").getInputStream();

            WasherRequest[] washers = mapper.readValue(inputStream, WasherRequest[].class);

            for (WasherRequest washer : washers) {
                try {
                    Optional<Washer> existing = washerRepository.findByName(washer.getName());
                    if (existing.isPresent()) {
                        logger.info("Skipping washer initialization: washer '{}' already exists", washer.getName());
                        continue;
                    }

                    washerService.verifyDuplicateWasher(washer);
                    washerService.addWasher(washer);
                    logger.info("Added washer: {}", washer.getName());

                } catch (Exception e) {
                    logger.warn("Failed to add washer '{}': {}", washer.getName(), e.getMessage());
                }
            }


        } catch (Exception e) {
            logger.error("Failed to initialize washers", e);
        }
    }



}
