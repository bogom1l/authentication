package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthenticationException;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtService;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginOperationProcessor extends BaseOperationProcessor<LoginInput> implements LoginOperation {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    protected LoginOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Either<ErrorsWrapper, LoginOutput> process(LoginInput input) {
        return Try.of(() -> login(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private LoginOutput login(LoginInput input) {
        log.info("Started LoginOperationProcessor with input: {}", input);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );
        // if authentication is successful (username and password are correct)
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        LoginOutput output = LoginOutput.builder()
                .token(jwtToken)
                .build();

        log.info("Ended LoginOperationProcessor with output: {}", output);
        return output;
    }
}
