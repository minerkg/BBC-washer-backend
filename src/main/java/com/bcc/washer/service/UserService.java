package com.bcc.washer.service;

import com.bcc.washer.domain.Role;
import com.bcc.washer.domain.User;
import com.bcc.washer.dto.UserRegistrationRequest;
import com.bcc.washer.exceptions.UserAlreadyExistsException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationRequest userRequest) {

        try {
            verifyDuplicateUser(userRequest);

            User newUser = User.builder()
                    .username(userRequest.username())
                    .password(passwordEncoder.encode(userRequest.password()))
                    .first_name(userRequest.first_name())
                    .last_name(userRequest.last_name())
                    .email(userRequest.email())
                    .phone(userRequest.phone_nr())
                    .role(Role.USER)
                    .nr_matricol("")
                    .build();

            return userRepository.save(newUser);
        }catch (DataIntegrityViolationException dive){
            throw new UserAlreadyExistsException("A user with the provided email,username,phone number already exists");
        }
    }


    public void verifyDuplicateUser(UserRegistrationRequest userRequest) {
        if ( userRepository.findByEmail(userRequest.email()).isPresent() ) {
            throw new UserAlreadyExistsException("User with email: "+userRequest.email() +" already exists");
        }
        if (userRepository.findByUsername(userRequest.username()).isPresent() ) {
            throw new UserAlreadyExistsException("User with username: "+userRequest.username() +" already exists");
        }
        if (userRepository.findByPhone(userRequest.phone_nr()).isPresent() ) {
            throw new UserAlreadyExistsException("User with phone: "+userRequest.phone_nr() +" already exists");
        }
    }

    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        user.setRole(newRole);
        return userRepository.save(user);
    }
}
