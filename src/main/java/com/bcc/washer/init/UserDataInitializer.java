package com.bcc.washer.init;

import com.bcc.washer.domain.user.User;
import com.bcc.washer.domain.user.Role;
import com.bcc.washer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDataInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void initUsersFromCsv() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("default-users.csv").getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // skip CSV header
                }

                String[] parts = line.split(",");

                if (parts.length != 8) {
                    System.out.println("[dataInit] Invalid user line (wrong column count): "+ line);
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
                    System.out.println("Unknown role "+ roleString + ", skipping user "+ username);
                    continue;
                }

                if (userRepository.findByUsername(username).isEmpty()) {
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
                    System.out.println("[dataInit] Created user "+ username);
                } else {
                    System.out.println("️[dataInit] User "+ username +" already exists, skipping user creation");
                }
            }

        } catch (Exception e) {
            System.out.println("[dataInit] Failed to initialize users from CSV" + e);
        }
    }
}
