package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthenticationException;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Registers a new user by converting RegisterInput to User model and saving it to the database
 * Returns the RegisterOutput model with the user id
 */
@Service
@Slf4j
public class RegisterOperationProcessor extends BaseOperationProcessor<RegisterInput> implements RegisterOperation {
    private final UserRepository userRepository;

    protected RegisterOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorsWrapper, RegisterOutput> process(RegisterInput input) {
        return Try.of(() -> register(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private void checkIfUserExistsByUsernameOrEmail(RegisterInput input) {
        userRepository.findByUsername(input.getUsername()).ifPresent(user -> {
            throw new AuthenticationException("Username already exists", HttpStatus.BAD_REQUEST);
        });

        userRepository.findByEmail(input.getEmail()).ifPresent(user -> {
            throw new AuthenticationException("Email already exists", HttpStatus.BAD_REQUEST);
        });
    }

    private RegisterOutput register(RegisterInput input) {
        log.info("Started RegisterOperationProcessor with input: {}", input);
        validateInput(input);
        checkIfUserExistsByUsernameOrEmail(input);

        User user = conversionService.convert(input, User.class);
        userRepository.save(user);
        RegisterOutput output = conversionService.convert(user, RegisterOutput.class);

        log.info("Ended RegisterOperationProcessor with output: {}", output);
        return output;
    }

}
