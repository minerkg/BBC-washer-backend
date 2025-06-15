package com.bcc.washer.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
}
