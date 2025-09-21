package com.example.user_service.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class UserCreateDTO {
    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]{1,20}$", message = "invalid format")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]{1,20}$", message = "invalid format")
    private String lastName;

    @NotNull(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be valid"
    )
    private String email;


    @NotNull(message = "Password is required")
    @Pattern(
            regexp = "^\\S{5,}$",
            message = "Password must be of at least length 7"
    )
    private String password;

    @NotNull(message = "Gender is required")
    @Pattern(
            regexp = "^(MALE|FEMALE|OTHER)$",
            message = "Gender must be MALE, FEMALE, or OTHER"
    )
    private String gender;

    @NotNull(message = "Role is required")
    @Pattern(
            regexp = "^(ADMIN|USER)$",
            message = "Role must be valid"
    )
    private String role;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @AssertTrue(message = "Age must be at least 13 years old")
    public boolean isAgeValid() {
        if (dob == null) return true;
        return Period.between(dob, LocalDate.now()).getYears() >= 13;
    }
}
