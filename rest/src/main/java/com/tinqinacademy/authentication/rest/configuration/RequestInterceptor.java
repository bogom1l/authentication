package com.tinqinacademy.authentication.rest.configuration;

import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import com.tinqinacademy.authentication.rest.interceptors.AccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RequestInterceptor implements WebMvcConfigurer {
    private final AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor)
                .addPathPatterns(RestApiRoutes.PROMOTE,
                        RestApiRoutes.DEMOTE,
                        RestApiRoutes.LOGOUT);
    }
}
