package com.tinqinacademy.authentication.api.operations.validatejwt;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ValidateJwtInput implements OperationInput {
    @NotNull
    private String authorizationHeader;
}
