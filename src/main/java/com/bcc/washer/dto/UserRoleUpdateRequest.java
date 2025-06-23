package com.bcc.washer.dto;

import com.bcc.washer.domain.user.Role;
import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private String username;
    private Role newRole;
}
