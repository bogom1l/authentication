package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import io.vavr.control.Either;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    private ResponseEntity<?> error(Either<ErrorsWrapper, ? extends OperationOutput> output) {
        return new ResponseEntity<>(output.getLeft().getErrors(), output.getLeft().getHttpStatus());
    }

    protected ResponseEntity<?> handle(Either<ErrorsWrapper, ? extends OperationOutput> output) {
        return output.isLeft() ? error(output) : new ResponseEntity<>(output.get(), HttpStatus.OK);
    }

    protected ResponseEntity<?> handleWithStatus(Either<ErrorsWrapper, ? extends OperationOutput> output, HttpStatus status) {
        return output.isLeft() ? error(output) : new ResponseEntity<>(output.get(), status);
    }

    protected ResponseEntity<?> handleWithJwt(Either<ErrorsWrapper, LoginOutput> output) {
        return output.isLeft() ? error(output) : ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, output.get().getToken()).build();
    }
}