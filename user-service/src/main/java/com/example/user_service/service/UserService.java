package com.example.user_service.service;

import com.example.user_service.dto.UserCreateDTO;
import com.example.user_service.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO dto);

    UserResponseDTO getUserById(long userId);

    UserResponseDTO getUserByEmail(String email);

    boolean verifyUserEmail(long userId, String token);

    void sendVerificationToken(long userId);
}
