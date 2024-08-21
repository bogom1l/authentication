package com.tinqinacademy.authentication.api.restroutes;

public class RestApiRoutes {
    public static final String API = "/api/v1";
    public static final String API_AUTH = API + "/auth";

    public static final String REGISTER = API_AUTH + "/register";
    public static final String LOGIN = API_AUTH + "/login";
    public static final String AUTH_CHECK_JWT = API_AUTH + "/validate-jwt";
    public static final String PROMOTE = API_AUTH + "/promote";
    public static final String DEMOTE = API_AUTH + "/demote";
    public static final String CHANGE_PASSWORD = API_AUTH + "/change-password";
    public static final String GET_ALL_USERS = API_AUTH + "/get-all-users";
    public static final String LOGOUT = API_AUTH + "/logout";
    public static final String RECOVER_PASSWORD = API_AUTH + "/recover-password";

}
