package com.tinqinacademy.authentication.api.operations.promote;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PromoteInput implements OperationInput {
    private String userId;
}
