package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthenticationException;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtTokenProvider;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Finds user by LoginInput model - username and password
 * Generates JWT token from JwtTokenProvider
 * Returns the token in the LoginOutput model, which then returns the token in the response header
 */
@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LoginInput> implements LoginOperation {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    protected LoginOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorsWrapper, LoginOutput> process(LoginInput input) {
        return Try.of(() -> login(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private User getUserAfterValidatingUsernameAndPassword(LoginInput input) {
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials", HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials", HttpStatus.BAD_REQUEST);
        }

        return user;
    }

    private LoginOutput login(LoginInput input) {
        log.info("Started LoginOperationProcessor with input: {}", input);
        validateInput(input);

        User user = getUserAfterValidatingUsernameAndPassword(input);

        String jwt = jwtTokenProvider.generateToken(user);

        LoginOutput output = LoginOutput.builder()
                .token(jwt)
                .build();

        log.info("Ended LoginOperationProcessor with output: {}", output);
        return output;
    }
}
