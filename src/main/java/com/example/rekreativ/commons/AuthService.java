package com.example.rekreativ.commons;

import com.example.rekreativ.auth.AuthUser;
import com.example.rekreativ.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtHandler jwtHandler) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtHandler = jwtHandler;
    }

    public HttpHeaders getJwtHeader(String username) {
        AuthUser authUser = new AuthUser(userService.findRawUserByUsername(username));

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", jwtHandler.generateJwtToken(authUser));

        return headers;
    }

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
