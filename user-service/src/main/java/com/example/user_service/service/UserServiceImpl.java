package com.example.user_service.service;

import com.example.kafka_configs.config.App_Constant;
import com.example.kafka_configs.event.UserVerifyEvent;
import com.example.user_service.dto.UserCreateDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.entity.UserEntity;
import com.example.user_service.entity.UserVerificationEntity;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.repository.UserVerificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserVerificationRepository verificationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BCryptPasswordEncoder passwordEncoder;


    private UserResponseDTO convertToDTO(UserEntity persistedEntity) {
        return UserResponseDTO.builder()
                .id(persistedEntity.getId())
                .firstName(persistedEntity.getFirstName())
                .lastName(persistedEntity.getLastName())
                .email(persistedEntity.getEmail())
                .dob(persistedEntity.getDob())
                .gender(persistedEntity.getGender())
                .role(persistedEntity.getRole())
                .isEmailVerified(persistedEntity.isEmailVerified())
                .createdAt(persistedEntity.getCreatedAt())
                .updatedAt(persistedEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public UserResponseDTO createUser(UserCreateDTO dto) {
        // checking duplicate email
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.info("duplicate email {}", dto.getEmail());
            throw new DuplicateEmailException("duplicate email " + dto.getEmail());
        }

        UserEntity userEntity = UserEntity.builder()
                .firstName(dto.getFirstName().toLowerCase())
                .lastName(dto.getLastName().toLowerCase())
                .email(dto.getEmail().toLowerCase())
                .password(passwordEncoder.encode(dto.getPassword())) // encoded password
                .dob(dto.getDob())
                .gender(dto.getGender())
                .role(dto.getRole())
                .build();

        // save user to db
        UserEntity persistedEntity = userRepository.save(userEntity);

        log.info("user saved with userId {}", persistedEntity.getId());

        return convertToDTO(persistedEntity);
    }

    @Transactional
    @Override
    public void sendVerificationToken(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Invalid userId " + userId));

        UserVerificationEntity verificationEntity = verificationRepository.findByUserId(userId)
                .orElseGet(UserVerificationEntity::new);

        String token = UUID.randomUUID().toString();

        verificationEntity.setUserId(userId);
        verificationEntity.setToken(token);
        verificationEntity.setExpiresAt(LocalDateTime.now().plusHours(2));

        // save to db
        verificationRepository.save(verificationEntity);

        log.info("userId {}, token {}", userEntity.getId(), token);

        // produce notification
        UserVerifyEvent event = UserVerifyEvent.builder()
                .userId(userEntity.getId())
                .name(userEntity.getFullName())
                .email(userEntity.getEmail())
                .token(token)
                .build();

        kafkaTemplate.send(App_Constant.USER_VERIFY_TOPIC, event);
    }

    @Override
    public UserResponseDTO getUserById(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Invalid userId " + userId));

        return convertToDTO(userEntity);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UserNotFoundException("Invalid email " + email);
        }

        return convertToDTO(userEntity);
    }

    @Override
    public boolean verifyUserEmail(long userId, String token) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        UserVerificationEntity userVerificationEntity = verificationRepository.findByUserId(userId).orElse(null);

        // invalid userId or token
        if (userEntity == null || userVerificationEntity == null) {
            log.info("invalid userId {} or token {}", userId, token);
            return false;
        }

        // invalid token
        if (!userVerificationEntity.getToken().equals(token)) {
            log.info("token mismatch; actual {}, expected {}", token, userVerificationEntity.getToken());
            return false;
        }

        // token expired
        if (userVerificationEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.info("token is expired");
            verificationRepository.delete(userVerificationEntity);
            return false;
        }

        log.info("account verified, userId {}", userEntity.getId());
        userEntity.setEmailVerified(true);
        verificationRepository.delete(userVerificationEntity);

        return true;
    }
}
