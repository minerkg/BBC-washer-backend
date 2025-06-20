// src/main/java/com/bcc/washer/controller/ResourceController.java
package com.bcc.washer.controller;

import com.bcc.washer.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user") // Base mapping for user-related endpoints
public class ResourceController { // Renamed from previous ResourceController for clarity, but use your actual class name.

    // Original /user/resource/test endpoint - combine with /user/me if they were separate files
    // If this was originally in a different controller, merge it here or ensure that controller exists.
    @GetMapping("/resource/test") // This will result in /user/resource/test
    public ResponseEntity<ApiResponse<String>> getTest() {
        try {
            return ResponseEntity.ok(new ApiResponse<>("test", "test" ));
        }catch (AuthenticationException aue){
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.UNAUTHORIZED.toString(), aue.getMessage() ), HttpStatus.UNAUTHORIZED);
        }

    }

    // Your new /user/me endpoint
    @GetMapping("/me") // This will result in /user/me
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(user);
    }
}