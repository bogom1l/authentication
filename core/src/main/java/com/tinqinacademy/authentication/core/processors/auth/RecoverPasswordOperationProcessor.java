package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.error.ErrorsWrapper;
import com.tinqinacademy.authentication.api.exceptions.AuthException;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authentication.core.errorhandler.ErrorHandler;
import com.tinqinacademy.authentication.core.mail.EmailService;
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

import java.util.Random;

@Service
@Slf4j
public class RecoverPasswordOperationProcessor extends BaseOperationProcessor<RecoverPasswordInput> implements RecoverPasswordOperation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService mailService;

    protected RecoverPasswordOperationProcessor(ConversionService conversionService, ErrorHandler errorHandler, Validator validator, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService mailService) {
        super(conversionService, errorHandler, validator);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public Either<ErrorsWrapper, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        return Try.of((() -> recoverPassword(input)))
                .toEither()
                .mapLeft(errorHandler::handleErrors);
    }

    private RecoverPasswordOutput recoverPassword(RecoverPasswordInput input) {
        log.info("Started RecoverPasswordOperationProcessor with input: {}", input);
        validateInput(input);

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new AuthException("User not found"));

        String newPassword = generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        mailService.sendEmail(user.getEmail(),
                "Recover Password",
                "Your new password is: " + newPassword);

        RecoverPasswordOutput output = RecoverPasswordOutput.builder().build();
        log.info("Ended RecoverPasswordOperationProcessor with output: {}", output);
        return output;
    }

    private String generateRandomPassword() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder password = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(letters.length());
            password.append(letters.charAt(index));
        }

        return password.toString();
    }
}
