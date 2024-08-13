package com.tinqinacademy.authentication.restexport;


import com.tinqinacademy.authentication.api.operations.validatejwt.ValidateJwtOutput;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "authentication")
public interface AuthRestExportClient {

    @PostMapping("/validate-jwt")
    @Headers({"Authorization: {authorizationHeader}"})
    ValidateJwtOutput validateJwt(@Param("authorizationHeader") String authHeader);

}
