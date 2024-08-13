package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final RegisterOperation registerOperation;
    private final LoginOperation loginOperation;
    private final ValidateJwtOperation validateJwtOperation;

    @PostMapping(RestApiRoutes.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        return handle(registerOperation.process(input));
    }

    @PostMapping(RestApiRoutes.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        return handleWithJwt(loginOperation.process(input));
    }

    @Operation(summary = "Validate JWT",
            description = "Swagger's login header always overrides this field, so it is not required.",
            hidden = false) // hidden = true, because we will only use this endpoint in the bff
    @PostMapping(RestApiRoutes.AUTH_CHECK_JWT)
    public ResponseEntity<?> validateJwt(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        //required = false, swagger's login header always overrides this field anyway
        ValidateJwtInput input = ValidateJwtInput.builder().authorizationHeader(authorizationHeader).build();
        return handle(validateJwtOperation.process(input));
    }

}
