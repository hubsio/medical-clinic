package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends MedicalException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
