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


}
