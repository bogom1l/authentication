package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final RegisterOperation registerOperation;
    private final LoginOperation loginOperation;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        return handle(registerOperation.process(input));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        return handleWithJwt(loginOperation.process(input));
    }

}
