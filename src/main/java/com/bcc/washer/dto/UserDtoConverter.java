package com.bcc.washer.dto;

import com.bcc.washer.domain.user.User;

public class UserDtoConverter extends BaseConverter<User, UserDto>{
    @Override
    User convertDtoToModel(UserDto dto) {
        return null;
    }

    @Override
    UserDto convertModelToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .last_name(user.getLast_name())
                .first_name(user.getFirst_name())
                .username(user.getUsername())
                .build();
    }
}
