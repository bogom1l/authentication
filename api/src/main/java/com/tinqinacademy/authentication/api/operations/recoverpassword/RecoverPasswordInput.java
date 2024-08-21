package com.tinqinacademy.authentication.api.operations.recoverpassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RecoverPasswordInput implements OperationInput {
    private String email;
}
