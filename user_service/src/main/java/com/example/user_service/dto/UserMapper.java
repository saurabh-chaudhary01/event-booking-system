package com.example.user_service.dto;

import com.example.user_service.entity.UserEntity;

public class UserMapper {
    public static UserResponseDTO mapToDTO(UserEntity entity) {
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .gender(entity.getGender())
                .dob(entity.getDob())
                .build();
    }
}
