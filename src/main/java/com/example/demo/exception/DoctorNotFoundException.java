package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MedicalException {
    public DoctorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
