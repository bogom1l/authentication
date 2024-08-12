package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtService;
import com.tinqinacademy.authentication.persistence.enums.Role;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterOperationProcessor extends BaseOperationProcessor<RegisterInput> implements RegisterOperation {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    protected RegisterOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(conversionService, errorHandler, validator);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorsWrapper, RegisterOutput> process(RegisterInput input) {
        return Try.of(() -> register(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private RegisterOutput register(RegisterInput input) {
        User user = User.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        //String jwtToken = jwtService.generateToken(user);

        return RegisterOutput.builder()
                .id(user.getId())
                .build();
    }

}
