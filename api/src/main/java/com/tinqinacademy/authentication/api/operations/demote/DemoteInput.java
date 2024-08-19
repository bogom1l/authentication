package com.tinqinacademy.authentication.api.operations.demote;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DemoteInput implements OperationInput {
    private String userId;
}
