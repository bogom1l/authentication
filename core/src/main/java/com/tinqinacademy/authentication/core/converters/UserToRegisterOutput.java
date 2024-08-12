package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.persistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserToRegisterOutput implements Converter<User, RegisterOutput> {
    @Override
    public RegisterOutput convert(User source) {
        log.info("Started Converter - User to RegisterOutput");

        RegisterOutput output = RegisterOutput.builder()
                .id(source.getId())
                .build();

        log.info("Ended Converter - User to RegisterOutput");
        return output;
    }
}
