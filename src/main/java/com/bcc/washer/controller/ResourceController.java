package com.bcc.washer.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/resource")
public class ResourceController {

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> getTest() {
        return ResponseEntity.ok(new ApiResponse<>("test", "test" ));
    }

}
