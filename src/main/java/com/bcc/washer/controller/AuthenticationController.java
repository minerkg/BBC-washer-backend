package com.bcc.washer.controller;

import com.bcc.washer.domain.User;
import com.bcc.washer.dto.UserRegistrationRequest;
import com.bcc.washer.exceptions.WasherStoreException;
import com.bcc.washer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//       TEST USER
//    {
//        "username": "user2",
//            "password": "password",
//            "first_name": "user2 firstn",
//            "last_name": "user2 lastn",
//            "email": "user2@example.com",
//            "phone_nr": "+1230987654"
//    }



}
