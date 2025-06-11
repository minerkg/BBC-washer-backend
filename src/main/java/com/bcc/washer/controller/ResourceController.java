
package com.bcc.washer.controller;

import com.bcc.washer.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user") //
public class ResourceController {


    @GetMapping("/resource/test") // This will result in /user/resource/test
    public ResponseEntity<ApiResponse<String>> getTest() {
        return ResponseEntity.ok(new ApiResponse<>("test", "test" ));
    }

    // Your new /user/me endpoint
    @GetMapping("/me") // This will result in /user/me
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(user);
    }
}