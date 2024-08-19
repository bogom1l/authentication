package com.tinqinacademy.authentication.api.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    private HttpStatus status;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}