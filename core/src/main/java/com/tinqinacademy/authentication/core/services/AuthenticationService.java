//package com.tinqinacademy.authentication.core.services;
//
//import com.tinqinacademy.authentication.api.exceptions.AuthenticationException;
//import com.tinqinacademy.authentication.core.security.JwtService;
//import com.tinqinacademy.authentication.persistence.enums.Role;
//import com.tinqinacademy.authentication.persistence.model.User;
//import com.tinqinacademy.authentication.api.operations.login.LoginInput;
//import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
//import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
//import com.tinqinacademy.authentication.persistence.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public LoginOutput register(RegisterInput request) {
//
//        var user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        var jwtToken = jwtService.generateToken(user);
//
//        return LoginOutput.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    public LoginOutput login(LoginInput request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//        // if authentication is successful (username and password are correct)
//        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AuthenticationException("User not found"));
//
//        var jwtToken = jwtService.generateToken(user);
//
//        return LoginOutput.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//}
