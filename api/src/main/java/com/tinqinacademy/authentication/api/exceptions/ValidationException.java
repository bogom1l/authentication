package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.error.Error;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private final List<Error> violations;

    public ValidationException(List<Error> violations) {
        this.violations = violations;
    }
}