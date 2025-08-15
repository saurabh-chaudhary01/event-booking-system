package com.example.user_service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    public long userId;
    public String firstName;
    public String lastName;
    public String email;
    public String gender;
    public LocalDate dob;
}
