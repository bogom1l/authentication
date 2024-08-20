package com.tinqinacademy.authentication.api.operations.getallusers;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetAllUsersOutput implements OperationOutput {
    private List<UserOutput> users;
}
