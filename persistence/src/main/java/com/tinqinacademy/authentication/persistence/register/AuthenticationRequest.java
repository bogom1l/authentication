package com.tinqinacademy.authentication.persistence.register;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    private String email;
    String password; //todo ? private
}
