package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends MediaclException {
    public DoctorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
