package com.application.service;

import com.application.config.Constants;
import com.application.config.JwtUtils;
import com.application.dto.*;
import com.application.entity.RefreshToken;
import com.application.entity.RoleEntity;
import com.application.entity.UserEntity;
import com.application.exception.ServerException;
import com.application.mapper.ObjectUtils;
import com.application.repository.RoleRepository;
import com.application.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AuthzServiceImpl implements AuthzService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private DataService dataService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletRequest fullRequest) {
        log.info("Entered into signIn Method()");
        log.info("Login Request body -- " + loginRequest.toString());
        Authentication authentication;
        try {
            if (ObjectUtils.isEmptyOrNull(loginRequest.getUsername()) || ObjectUtils.isEmptyOrNull(loginRequest.getPassword())) {
                throw new ServerException("Invalid UserName and Password", Constants.INTERNAL_SERVER,
                        fullRequest.getRequestURL().toString(), "authenticateUser()");
            } else {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            }
        } catch (Exception e) {
            throw new ServerException("Incorrect UserName and Password", Constants.INTERNAL_SERVER,
                    fullRequest.getRequestURL().toString(), "authenticateUser()");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwtToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles, "LoggedIn Successfully!"));
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest, HttpServletRequest fullRequest) {
        log.info("Entered RegisterUser() method");
        log.info("SignUpRequest -- " + signUpRequest.toString());
        log.info("Http Servlet Request -- " + fullRequest.toString());

        if (ObjectUtils.isEmptyOrNull(signUpRequest.getUsername()) || ObjectUtils.isEmptyOrNull(signUpRequest.getEmail())) {
            throw new ServerException("Invalid UserName/Email", Constants.INTERNAL_SERVER,
                    fullRequest.getRequestURL().toString(), "registerUser()");
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Username is already taken!", 0, ""));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Email is already in use!", 0, ""));
        }

        UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(Constants.ERole.ROLE_USER)
                    .orElseThrow(() -> new ServerException(Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                            fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleEntity adminRole = roleRepository.findByName(Constants.ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ServerException("Admin " + Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));
                        if (Boolean.TRUE.equals(adminRole.isActive())) {
                            roles.add(adminRole);
                        } else {
                            throw new ServerException("Admin " + Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                        break;
                    case "mod":
                        RoleEntity modRole = roleRepository.findByName(Constants.ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new ServerException("Mod " + Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));

                        if (Boolean.TRUE.equals(modRole.isActive())) {
                            roles.add(modRole);
                        } else {
                            throw new ServerException("Mod " + Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByName(Constants.ERole.ROLE_USER)
                                .orElseThrow(() -> new ServerException(Constants.ROLE_NOT_FOUND, Constants.INTERNAL_SERVER,
                                        fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD));

                        if (Boolean.TRUE.equals(userRole.isActive())) {
                            roles.add(userRole);
                        } else {
                            throw new ServerException(Constants.ROLE_NOT_ACTIVE, Constants.INTERNAL_SERVER,
                                    fullRequest.getRequestURL().toString(), Constants.REGISTER_USER_METHOD);
                        }
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!", 0, ""));
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new ServerException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
