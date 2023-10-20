package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class IllegalDoctorIdChangeException extends MedicalException {
    public IllegalDoctorIdChangeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
