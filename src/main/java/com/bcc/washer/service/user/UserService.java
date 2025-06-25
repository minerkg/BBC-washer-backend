package com.bcc.washer.service.user;

import com.bcc.washer.domain.user.Role;
import com.bcc.washer.domain.user.User;
import com.bcc.washer.dto.UpdateUserDto;
import com.bcc.washer.dto.UserDto;
import com.bcc.washer.dto.UserDtoConverter;
import com.bcc.washer.dto.UserRegistrationRequest;
import com.bcc.washer.exceptions.UserAlreadyExistsException;
import com.bcc.washer.exceptions.UserNotFoundException;
import com.bcc.washer.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDtoConverter userDtoConverter;

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
    public void updateUserRole(String username, Role newRole) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(a -> System.out.println("Authority: " + a.getAuthority()));
        if (authentication == null || !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can update user roles.");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        user.setRole(newRole);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(UpdateUserDto updateData) {
        User user = userRepository.findByUsername(updateData.getUsername()).orElseThrow(()-> new UserNotFoundException("User not found with username: " + updateData.getUsername()));
        user.setFirst_name(updateData.getFirst_name());
        user.setLast_name(updateData.getLast_name());
        user.setEmail(updateData.getEmail());
        user.setPhone(updateData.getPhone());

        return user;
    }
}
