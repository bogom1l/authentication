package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthController {

    //private final AuthenticationService authenticationService;
    private final RegisterOperation registerOperation;
    private final LoginOperation loginOperation;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        //return new ResponseEntity<>(authenticationService.register(request), HttpStatus.OK);
        Either<ErrorsWrapper, RegisterOutput> output = registerOperation.process(input);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        Either<ErrorsWrapper, LoginOutput> output = loginOperation.process(input);
        if (output.isRight()) {
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, output.get().getToken()).build();
        } else {
            return new ResponseEntity<>(output.getLeft(), HttpStatus.UNAUTHORIZED);
        }
    }

}
