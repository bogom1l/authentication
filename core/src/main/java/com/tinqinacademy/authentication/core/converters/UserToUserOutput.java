package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.getallusers.UserOutput;
import com.tinqinacademy.authentication.persistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserToUserOutput implements Converter<User, UserOutput> {
    @Override
    public UserOutput convert(User source) {
        log.info("Started Converter - User to UserOutput");

        UserOutput target = UserOutput.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .role(String.valueOf(source.getRole()))
                .email(source.getEmail())
                .phoneNumber(source.getPhoneNumber())
                .build();

        log.info("Ended Converter - User to UserOutput");
        return target;
    }
}
