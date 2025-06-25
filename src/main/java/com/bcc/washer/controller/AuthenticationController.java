package com.bcc.washer.controller;

import com.bcc.washer.domain.user.User;
import com.bcc.washer.dto.*;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateUserDto updateData){
        try {
            User updatedUser = userService.updateUser(updateData);
            return ResponseEntity.ok().body(new ApiResponse<>("updated user", updatedUser));
        }catch (RuntimeException e){
            return ResponseEntity.ok().body(new ApiResponse<>("user update failed",e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRoleUpdateRequest request) {
        try {
            userService.updateUserRole(request.getUsername(), request.getNewRole());
            return ResponseEntity.ok().body(new ApiResponse<>("role changed",null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        }
    }



}
