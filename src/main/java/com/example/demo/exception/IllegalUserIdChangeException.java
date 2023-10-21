package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class IllegalUserIdChangeException extends MedicalException {
    public IllegalUserIdChangeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
