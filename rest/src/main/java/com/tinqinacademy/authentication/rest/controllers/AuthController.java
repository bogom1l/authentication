package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.core.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginDto loginDto) {
//
//        String token = authService.login(request, loginDto);
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setAccessToken(token);
//
//        return new ResponseEntity<>(authResponse, HttpStatus.OK);
//    }

}
