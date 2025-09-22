package com.example.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String role;
    private LocalDate dob;
    private boolean isEmailVerified;
    private Instant createdAt;
    private Instant updatedAt;
}
