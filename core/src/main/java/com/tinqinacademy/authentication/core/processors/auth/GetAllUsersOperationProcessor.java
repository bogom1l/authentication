package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersInput;
import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersOperation;
import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetAllUsersOperationProcessor extends BaseOperationProcessor<GetAllUsersInput> implements GetAllUsersOperation {
    private final UserRepository userRepository;

    protected GetAllUsersOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorsWrapper, GetAllUsersOutput> process(GetAllUsersInput input) {
        return Try.of(() -> getAllUsers(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private GetAllUsersOutput getAllUsers(GetAllUsersInput input) {
        log.info("Started GetAllUsersOperationProcessor with input: {}", input);

        List<User> users = userRepository.findAll();

        GetAllUsersOutput output = conversionService.convert(users, GetAllUsersOutput.class);

        log.info("Ended GetAllUsersOperationProcessor with output: {}", output);
        return output;
    }
}
