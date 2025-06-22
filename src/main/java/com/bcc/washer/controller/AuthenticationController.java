package com.bcc.washer.controller;

import com.bcc.washer.dto.PasswordChangeRequest;
import com.bcc.washer.dto.UserRegistrationRequest;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser (@RequestBody UserRegistrationRequest registerRequest){

        try {
            var newUser = userService.registerUser(registerRequest);
            return ResponseEntity.ok().body(new ApiResponse<>("user created",newUser));
        }catch (WasherStoreException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>("user already exists",e.getMessage()));
        }
    }

    @PostMapping("/register-batch")
    public ResponseEntity<ApiResponse<?>> registerMultipleUsers(@RequestBody List<UserRegistrationRequest> requests) {
        List<Object> createdUsers = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (UserRegistrationRequest request : requests) {
            try {
                var user = userService.registerUser(request);
                createdUsers.add(user);
            } catch (WasherStoreException e) {
                errors.add("Failed to create user '" + request.username() + "': " + e.getMessage());
            }
        }
        ApiResponse<Object> response = new ApiResponse<>(
                errors.isEmpty() ? "All users created" : "Some users failed",
                Map.of(
                        "created", createdUsers,
                        "errors", errors
                )
        );
        return ResponseEntity
                .status(errors.isEmpty() ? HttpStatus.OK : HttpStatus.MULTI_STATUS)
                .body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request, Authentication authentication){
        try {
            System.out.println(request);
            userService.changePassword(authentication.getName(), request.getCurrentPassword(),request.getNewPassword());
            return ResponseEntity.ok().body(new ApiResponse<>("password changed",null));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse<>("password change failed",e.getMessage()));
        }

    }


}
