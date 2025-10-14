package com.application.config;

import com.application.exception.ServerException;
import com.application.service.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // List of endpoints to exclude from JWT validation
    private static final String[] PUBLIC_URLS = {
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/refresh-token"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();

            // Skip JWT validation for public URLs
            for (String publicUrl : PUBLIC_URLS) {
                if (path.startsWith(publicUrl)) {
                    filterChain.doFilter(request, response);
                    return;  // exit filter here
                }
            }

            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // No JWT or invalid token for protected routes
                throw new ServerException("Pass Valid Bearer Token", HttpStatus.UNAUTHORIZED.series().name(),
                        request.getRequestURL().toString(), "Authorize() Method",
                        String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            }
        } catch (Exception e) {
            logger.warn("Cannot set user authentication: {}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
            return; // Stop filter chain since unauthorized
        }
        filterChain.doFilter(request, response);
    }

    //Get JWT from the Authorization header (by removing Bearer prefix)
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
