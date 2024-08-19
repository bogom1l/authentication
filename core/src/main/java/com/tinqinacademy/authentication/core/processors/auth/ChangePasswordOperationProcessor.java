package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthException;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
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
public class ChangePasswordOperationProcessor extends BaseOperationProcessor<ChangePasswordInput> implements ChangePasswordOperation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected ChangePasswordOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<ErrorsWrapper, ChangePasswordOutput> process(ChangePasswordInput input) {
        return Try.of(() -> changePassword(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private ChangePasswordOutput changePassword(ChangePasswordInput input) {
        log.info("Started ChangePasswordOperationProcessor with input: {}", input);
        validateInput(input);

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new AuthException("User not found"));

        String currentPassword = user.getPassword();
        if(!passwordEncoder.matches(input.getOldPassword(), currentPassword)){
            throw new AuthException("Old password is incorrect.");
        }

        String newPassword = passwordEncoder.encode(input.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);

        ChangePasswordOutput output = ChangePasswordOutput.builder().build();
        log.info("Ended ChangePasswordOperationProcessor with output: {}", output);
        return output;
    }

}
