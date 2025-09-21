package com.example.user_service.controller;

import com.example.user_service.dto.UserCreateDTO;
import com.example.user_service.dto.UserResponseDTO;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO userResponse = userService.createUser(dto);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/send-email")
    public ResponseEntity<Void> sendVerificationMail(@PathVariable(value = "userId") long userId) {
        userService.sendVerificationToken(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/verify-email")
    public ResponseEntity<Void> verifyEmail(@PathVariable(value = "userId") long userId, @RequestParam(value = "token") String token) {
        boolean success = userService.verifyUserEmail(userId, token);
        if (!success) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(value = "userId") Long userId) {
        log.info("querying for userId {}", userId);
        UserResponseDTO userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @ExceptionHandler({UserNotFoundException.class, DuplicateEmailException.class})
    public ResponseEntity<String> handleUserValidationErrors(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
