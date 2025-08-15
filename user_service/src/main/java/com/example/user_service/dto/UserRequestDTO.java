package com.example.user_service.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotNull(message = "First Name is required")
    @Pattern(regexp = "^[a-z]{2,15}$", message = "Invalid First Name format")
    private String firstName;

    @NotNull(message = "Last Name is required")
    @Pattern(regexp = "^[a-z]{2,15}$", message = "Invalid Last Name format")
    private String lastName;

    @NotNull(message = "Email is required")
    @Pattern(
            regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$",
            message = "Invalid email format (only lowercase letters allowed)"
    )
    private String email;

    @NotNull(message = "Gender is required")
    @Pattern(
            regexp = "^(Male|Female|Other)$",
            message = "Gender must be Male, Female, or Other"
    )
    private String gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @AssertTrue(message = "Age must be at least 12 years")
    public boolean isAtLeast12YearsOld() {
        if (dob == null) return true;
        return dob.plusYears(12).isBefore(LocalDate.now())
                || dob.plusYears(12).isEqual(LocalDate.now());
    }
}
