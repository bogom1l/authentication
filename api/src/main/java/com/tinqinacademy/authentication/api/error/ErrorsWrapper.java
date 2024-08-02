package com.tinqinacademy.authentication.api.error;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorsWrapper {
    private List<Error> errors;
    private HttpStatus httpStatus;
}
