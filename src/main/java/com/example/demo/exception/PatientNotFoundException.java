package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends ApiException {
    public PatientNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
