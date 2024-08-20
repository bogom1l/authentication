package com.tinqinacademy.authentication.api.operations.logout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LogoutInput implements OperationInput {

    @JsonIgnore
    private String token;
}
