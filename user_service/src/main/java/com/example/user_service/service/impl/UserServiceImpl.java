package com.example.user_service.service.impl;

import com.example.user_service.dto.UserMapper;
import com.example.user_service.dto.UserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (doesUserExists(dto.getEmail())) {
            throw new DuplicateEmailException("duplicate email " + dto.getEmail());
        }

        UserEntity userEntity = UserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .gender(dto.getGender())
                .dob(dto.getDob())
                .build();

        UserEntity persistedEntity = userRepository.save(userEntity);

        return UserMapper.mapToDTO(persistedEntity);
    }

    @Override
    public boolean doesUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponseDTO getUserById(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("invalid userId " + userId));

        return UserMapper.mapToDTO(userEntity);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("invalid email " + email));

        return UserMapper.mapToDTO(userEntity);
    }
}
