package com.example.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> requestBodyValidationException(
            MethodArgumentNotValidException exception) {
        Map<String, Object> errors = new TreeMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> errors
                .put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return errors;
    }
}
