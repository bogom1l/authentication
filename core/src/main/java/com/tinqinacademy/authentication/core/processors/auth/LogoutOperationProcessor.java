package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.logout.LogoutInput;
import com.tinqinacademy.authentication.api.operations.logout.LogoutOperation;
import com.tinqinacademy.authentication.api.operations.logout.LogoutOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.model.BlacklistedToken;
import com.tinqinacademy.authentication.persistence.repository.BlacklistedTokenRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogoutOperationProcessor extends BaseOperationProcessor<LogoutInput> implements LogoutOperation {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    protected LogoutOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, BlacklistedTokenRepository blacklistedTokenRepository) {
        super(conversionService, errorHandler, validator);
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Override
    public Either<ErrorsWrapper, LogoutOutput> process(LogoutInput input) {
        return Try.of(() -> logout(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private LogoutOutput logout(LogoutInput input) {
        log.info("Started LogoutOperationProcessor with input: {}", input);
        validateInput(input);

        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                .token(input.getToken())
                .build();

        blacklistedTokenRepository.save(blacklistedToken);

        LogoutOutput output = LogoutOutput.builder()
                .message("Successfully logged out.")
                .build();

        log.info("Ended LogoutOperationProcessor with output: {}", output);
        return output;
    }
}
