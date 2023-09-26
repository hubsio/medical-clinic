package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class UnknownException extends ApiException {
    public UnknownException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
