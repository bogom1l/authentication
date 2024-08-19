package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthException;
import com.tinqinacademy.authentication.api.operations.demote.DemoteInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOutput;
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
public class DemoteOperationProcessor extends BaseOperationProcessor<DemoteInput> implements DemoteOperation {
    private final UserRepository userRepository;

    protected DemoteOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorsWrapper, DemoteOutput> process(DemoteInput input) {
        return Try.of(() -> demote(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private DemoteOutput demote(DemoteInput input) {
        log.info("Started DemoteOperationProcessor with input: {}", input);
        validateInput(input);

        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new AuthException("User not found"));

        if (user.getRole().equals(Role.USER)) {    // potential problem -> use .toString()
            throw new AuthException("User is already with user role");
        }

        user.setRole(Role.USER);
        userRepository.save(user);

        DemoteOutput output = DemoteOutput.builder().build();
        log.info("Ended DemoteOperationProcessor with output: {}", output);
        return output;
    }
}
