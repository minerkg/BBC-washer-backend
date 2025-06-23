package com.bcc.washer.init;

import com.bcc.washer.controller.TimeSlotController;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.domain.user.Role;
import com.bcc.washer.repository.TimeSlotRepository;
import com.bcc.washer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
            logger.warn("Skipping user initialization: " + existingUsers + " already exist in the database.");
            userRepository.findAll().forEach(user -> logger.info("Existing user: " + user.getUsername()));
            return;
        } else {
            logger.info("Starting user initialization");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("default-users.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 8) {
                    logger.error("Invalid user line (wrong column count): " + line);
                    continue;
                }

                String username = parts[0].trim();
                String password = parts[1].trim(); // already hashed
                String firstName = parts[2].trim();
                String lastName = parts[3].trim();
                String email = parts[4].trim();
                String phone = parts[5].trim();
                String nrMatricol = parts[6].trim();
                String roleString = parts[7].trim().toUpperCase();

                Role role;
                try {
                    role = Role.valueOf(roleString);
                } catch (IllegalArgumentException e) {
                    logger.warn("Unknown role " + roleString + ", skipping user " + username);
                    continue;
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setNr_matricol(nrMatricol);
                user.setRole(role);
                userRepository.save(user);
                logger.info("Created user " + username);

            }

        } catch (Exception e) {
            logger.error("Failed to initialize users from CSV" + e);
        }
    }
}
