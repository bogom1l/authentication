package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.persistence.enums.Role;
import com.tinqinacademy.authentication.persistence.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterInputToUser implements Converter<RegisterInput, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(RegisterInput source) {
        log.info("Started Converter - RegisterInput to User");

        User user = User.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .birthdate(LocalDate.parse(source.getBirthdate()))
                .phoneNumber(source.getPhoneNumber())
                .email(source.getEmail())
                .password(passwordEncoder.encode(source.getPassword()))
                .role(Role.USER)
                .username(source.getUsername())
                .build();

        log.info("Ended Converter - RegisterInput to User");
        return user;
    }
}
