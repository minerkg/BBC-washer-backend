package com.bcc.washer.init;

import com.bcc.washer.domain.user.Role;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initUsers() {
        createUserIfNotExists("client", "$2a$10$wUVfThvKOFm9U1U0vyvpw.DJneCC3tdImaFk/IOJ3X5cLmr5f1RJO",
                "John-client", "Doe", "jdoe-client@example.com", "0741000000", "123456", Role.USER);

        createUserIfNotExists("admin", "$2a$10$wUVfThvKOFm9U1U0vyvpw.DJneCC3tdImaFk/IOJ3X5cLmr5f1RJO",
                "John-admin", "Doe", "jdoe-admin@example.com", "0742000000", "123456", Role.ADMIN);

        createUserIfNotExists("employee", "$2a$10$wUVfThvKOFm9U1U0vyvpw.DJneCC3tdImaFk/IOJ3X5cLmr5f1RJO",
                "John-employee", "Doe", "jdoe-employee@example.com", "0743000000", "123456", Role.EMPLOYEE);
    }

    private void createUserIfNotExists(String username, String hashedPassword, String firstName,
                                       String lastName, String email, String phone,
                                       String nrMatricol, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setNr_matricol(nrMatricol);
            user.setRole(role);
            userRepository.save(user);
            System.out.println("[dataInit] Created default user: <<"+ username + ">>");
        } else {
            System.out.println("[dataInit] User <<" + username + ">> already exists, skipping creation");
        }
    }
}
