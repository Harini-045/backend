package com.application.service;

import com.application.dto.LoginRequest;
import com.application.dto.SignupRequest;
import com.application.dto.TokenRefreshRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthzService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletRequest fullRequest);

    ResponseEntity<?> registerUser(SignupRequest signUpRequest, HttpServletRequest fullRequest);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request);

}