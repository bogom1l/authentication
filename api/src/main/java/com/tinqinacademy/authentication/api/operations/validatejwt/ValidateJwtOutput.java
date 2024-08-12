package com.tinqinacademy.authentication.api.operations.validatejwt;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ValidateJwtOutput implements OperationOutput {
    private Boolean isValid;
}
