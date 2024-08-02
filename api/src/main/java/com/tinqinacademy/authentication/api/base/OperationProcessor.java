package com.tinqinacademy.authentication.api.base;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import io.vavr.control.Either;

public interface OperationProcessor<I extends OperationInput, O extends OperationOutput> {
    Either<ErrorsWrapper, O> process(I input);
}