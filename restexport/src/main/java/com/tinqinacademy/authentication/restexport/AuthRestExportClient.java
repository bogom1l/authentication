package com.tinqinacademy.authentication.restexport;


import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOutput;
import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentication")
public interface AuthRestExportClient {

    @PostMapping(RestApiRoutes.AUTH_CHECK_JWT)
    @Headers({"Authorization: {authorizationHeader}"})
    ValidateJwtOutput validateJwt(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

}
