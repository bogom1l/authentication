package com.tinqinacademy.authentication.api.operations.checkuser;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CheckUserOutput implements OperationOutput {
    private String firstName; // sample class
}
