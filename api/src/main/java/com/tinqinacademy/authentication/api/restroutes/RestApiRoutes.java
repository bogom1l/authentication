package com.tinqinacademy.authentication.api.restroutes;

public class RestApiRoutes {
    public static final String API = "/api/v1";
    public static final String API_AUTH = API + "/auth";

    public static final String REGISTER = API_AUTH + "/register";
    public static final String LOGIN = API_AUTH + "/login";
    public final static String AUTH_CHECK_JWT = API_AUTH + "/validate-jwt";
}
