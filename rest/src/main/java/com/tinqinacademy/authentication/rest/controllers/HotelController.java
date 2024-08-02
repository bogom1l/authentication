package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelController {

    @GetMapping(RestApiRoutes.API_HOTEL)
    public ResponseEntity<?> getHotel() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
