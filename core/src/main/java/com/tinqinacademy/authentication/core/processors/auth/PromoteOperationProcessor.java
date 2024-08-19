package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthException;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.enums.Role;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PromoteOperationProcessor extends BaseOperationProcessor<PromoteInput> implements PromoteOperation {
    private final UserRepository userRepository;

    protected PromoteOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorsWrapper, PromoteOutput> process(PromoteInput input) {
        return Try.of(() -> promote(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private PromoteOutput promote(PromoteInput input) {
        log.info("Started PromoteOperationProcessor with input: {}", input);
        validateInput(input);

        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.getRole().equals(Role.ADMIN)) {    // potential problem -> use .toString()
            throw new AuthException("User is already an admin");
        }

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        PromoteOutput output = PromoteOutput.builder().build();
        log.info("Ended PromoteOperationProcessor with output: {}", output);
        return output;
    }

}
