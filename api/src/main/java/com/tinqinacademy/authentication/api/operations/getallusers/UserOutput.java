package com.tinqinacademy.authentication.api.operations.getallusers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserOutput {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
}
