package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.processors.base.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtService;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidateJwtOperationProcessor extends BaseOperationProcessor<ValidateJwtInput> implements ValidateJwtOperation {

    private final JwtService jwtService;

    protected ValidateJwtOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, JwtService jwtService) {
        super(conversionService, errorHandler, validator);
        this.jwtService = jwtService;
    }

    @Override
    public Either<ErrorsWrapper, ValidateJwtOutput> process(ValidateJwtInput input) {
        return Try.of(() -> validateJwt(input))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private ValidateJwtOutput validateJwt(ValidateJwtInput input) {
        log.info("Started ValidateJwtOperationProcessor with input: {}", input);
        validateInput(input);

        String jwt = input.getAuthHeader().substring(7);

        ValidateJwtOutput output = ValidateJwtOutput.builder()
                .isValid(jwtService.isTokenValidWithUserId(jwt))
                .build();

        log.info("Ended ValidateJwtOperationProcessor with output: {}", output);
        return output;
    }
}
