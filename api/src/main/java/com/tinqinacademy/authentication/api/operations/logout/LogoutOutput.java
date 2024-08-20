package com.tinqinacademy.authentication.api.operations.logout;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LogoutOutput implements OperationOutput {
    private String message;
}
