package com.application.controller;

import com.application.dto.LoginRequest;
import com.application.dto.SignupRequest;
import com.application.dto.TokenRefreshRequest;
import com.application.service.AuthzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@Log4j2
public class AuthzController {

    @Autowired
    AuthzService authzService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              HttpServletRequest fullRequest) {
        return authzService.authenticateUser(loginRequest, fullRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          HttpServletRequest fullRequest) {
        return authzService.registerUser(signUpRequest, fullRequest);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authzService.refreshToken(request);
    }

}
