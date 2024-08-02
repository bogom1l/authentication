package com.tinqinacademy.authentication.api.operations.authentication.checkuser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CheckUserInput implements OperationInput {
    private String firstName;
}
