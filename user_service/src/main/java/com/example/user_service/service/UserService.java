package com.example.user_service.service;

import com.example.user_service.dto.UserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);

    boolean doesUserExists(String email);

    UserResponseDTO getUserById(long userId);

    UserResponseDTO getUserByEmail(String email);
}
