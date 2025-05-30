package com.bcc.washer.dto;

public record UserRegistrationRequest(
    String username,
    String password,
    String first_name,
    String last_name,
    String email,
    String phone_nr
) {
}
