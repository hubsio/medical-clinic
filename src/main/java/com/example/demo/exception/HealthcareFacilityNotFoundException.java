package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class HealthcareFacilityNotFoundException extends MedicalException {
    public HealthcareFacilityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
