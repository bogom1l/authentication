package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersInput;
import com.tinqinacademy.authentication.api.operations.getallusers.GetAllUsersOperation;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteOperation;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final RegisterOperation registerOperation;
    private final LoginOperation loginOperation;
    private final ValidateJwtOperation validateJwtOperation;
    private final PromoteOperation promoteOperation;
    private final DemoteOperation demoteOperation;
    private final ChangePasswordOperation changePasswordOperation;
    private final GetAllUsersOperation getAllUsersOperation;

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

    @Operation(summary = "Promote",
            description = "Promote a user to admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(RestApiRoutes.PROMOTE)
    public ResponseEntity<?> promote(@RequestBody PromoteInput input) {
        return handle(promoteOperation.process(input));
    }

    @Operation(summary = "Demote",
            description = "Demote a user from admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(RestApiRoutes.DEMOTE)
    public ResponseEntity<?> demote(@RequestBody DemoteInput input) {
        return handle(demoteOperation.process(input));
    }

    @Operation(summary = "Change password",
            description = "Change password of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping(RestApiRoutes.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordInput input) {
        return handle(changePasswordOperation.process(input));
    }

    @Operation(summary = "Get all users",
            description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping(RestApiRoutes.GET_ALL_USERS)
    public ResponseEntity<?> getAllUsers(@RequestBody(required = false)GetAllUsersInput input) {
        return handle(getAllUsersOperation.process(input));
    }
}
