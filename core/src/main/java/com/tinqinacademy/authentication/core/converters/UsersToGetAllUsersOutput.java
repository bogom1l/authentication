package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersOutput;
import com.tinqinacademy.authentication.api.operations.getallusers.UserOutput;
import com.tinqinacademy.authentication.persistence.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UsersToGetAllUsersOutput implements Converter<List<User>, GetAllUsersOutput> {

    private final ConversionService conversionService;

    @Lazy
    public UsersToGetAllUsersOutput(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public GetAllUsersOutput convert(List<User> source) {
        log.info("Started Converter - Users to GetAllUsersOutput");

        List<UserOutput> users = source.stream()
                .map(user -> conversionService.convert(user, UserOutput.class))
                .toList();

        GetAllUsersOutput target = GetAllUsersOutput.builder()
                .users(users)
                .build();

        log.info("Ended Converter - Users to GetAllUsersOutput");
        return target;
    }
}
