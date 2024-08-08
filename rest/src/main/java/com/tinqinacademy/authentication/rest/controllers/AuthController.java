package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
//
//        String token = authService.login(loginDto);
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setAccessToken(token);
//
//        return new ResponseEntity<>(authResponse, HttpStatus.OK);
//    }

}
