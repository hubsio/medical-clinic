package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class IllegalDoctorIdChangeException extends MediaclException{
    public IllegalDoctorIdChangeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
