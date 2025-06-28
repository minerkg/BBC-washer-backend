package com.bcc.washer.controller;

import com.bcc.washer.domain.user.User;
import com.bcc.washer.dto.UserRoleUpdateRequest;
import com.bcc.washer.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRoleUpdateRequest request) {
        try {
            System.out.println(request);
            userService.updateUserRole(request.getUsername(), request.getNewRole());
            return ResponseEntity.ok().body(new ApiResponse<>("role changed",null));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        }
    }
}
