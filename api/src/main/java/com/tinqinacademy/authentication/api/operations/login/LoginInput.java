package com.tinqinacademy.authentication.api.operations.login;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginInput implements OperationInput {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
