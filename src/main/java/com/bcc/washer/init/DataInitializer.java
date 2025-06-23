package com.bcc.washer.init;

import com.bcc.washer.controller.TimeSlotController;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.UserRepository;
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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(TimeSlotController.class);

    private final UserRepository userRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    public void run(ApplicationArguments args) {
        initUsers();
//        initTimeSlots();
    }

    public void initUsers() {
        long existingUsers = userRepository.count();
        if (existingUsers > 0) {
            logger.warn("Skipping user initialization: {} already exist", existingUsers);
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
}
