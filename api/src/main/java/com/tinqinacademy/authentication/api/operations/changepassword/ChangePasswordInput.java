package com.tinqinacademy.authentication.api.operations.changepassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChangePasswordInput implements OperationInput {
    private String email;

    private String oldPassword;

    private String newPassword;
}
