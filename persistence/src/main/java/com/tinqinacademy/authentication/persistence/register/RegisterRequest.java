package com.tinqinacademy.authentication.persistence.register;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
//    private String phoneNumber;
//    private String birthdate;
}
