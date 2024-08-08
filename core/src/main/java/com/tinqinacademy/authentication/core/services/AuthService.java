package com.tinqinacademy.authentication.core.services;

import com.tinqinacademy.authentication.persistence.login.LoginInput;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    String login(HttpServletRequest req, LoginInput loginInput);
}
