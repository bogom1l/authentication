package com.tinqinacademy.authentication.rest.interceptors;

import com.tinqinacademy.authentication.api.restroutes.RestApiRoutes;
import com.tinqinacademy.authentication.core.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AccessInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    private final String[] adminRoutes = {RestApiRoutes.PROMOTE, RestApiRoutes.DEMOTE};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
        String requestURI = request.getRequestURI();
        if(!isRouteAdminRoute(requestURI)) {
            return false;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(401, "Unauthorized");
            return false;
        }

        String jwt = authorizationHeader.substring(7);
        if (!jwtTokenProvider.validateToken(jwt)) {
            response.sendError(401, "Unauthorized");
            return false;
        }
        if(!jwtTokenProvider.extractRole(jwt).equals("ADMIN")) {
            response.sendError(403, "Forbidden");
            return false;
        }

        return true;
    }

    private boolean isRouteAdminRoute(String requestURI) {
        return Arrays.asList(adminRoutes).contains(requestURI);
    }
}