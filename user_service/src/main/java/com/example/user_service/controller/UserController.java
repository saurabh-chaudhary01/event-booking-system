package com.example.user_service.controller;

import com.example.user_service.dto.UserRequestDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/")
    public ResponseEntity<UserResponseDTO> getUserByEmail(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long userId
    ) {
        if (email != null) {
            UserResponseDTO response = userService.getUserByEmail(email);
            return ResponseEntity.ok(response);
        }

        if (userId != null) {
            UserResponseDTO response = userService.getUserById(userId);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    public String duplicateEmailExceptionHandler(DuplicateEmailException ex) {
        return ex.getMessage();
    }
}
