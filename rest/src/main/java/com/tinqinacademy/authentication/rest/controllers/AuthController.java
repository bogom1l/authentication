package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOperation;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtInput;
import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOperation;
import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final RegisterOperation registerOperation;
    private final LoginOperation loginOperation;
    private final ValidateJwtOperation validateJwtOperation;

    @Operation(summary = "Register",
            description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(RestApiRoutes.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        return handleWithStatus(registerOperation.process(input), HttpStatus.CREATED);
    }

    @Operation(summary = "Login",
            description = "Login to user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
