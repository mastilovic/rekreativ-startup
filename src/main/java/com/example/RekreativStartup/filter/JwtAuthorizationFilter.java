package com.example.RekreativStartup.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.RekreativStartup.Service.UserService;
import com.example.RekreativStartup.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET_KEY = "jKZGcDKSY1fcmFAwxVsof5GicOdsz9sD";

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")){
            response.setStatus(OK.value());
        } else {

            if (request.getServletPath().equals("/api/user/login")) {
                filterChain.doFilter(request, response);
            } else {

                String authorizationHeader = request.getHeader(AUTHORIZATION);
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    try {
                        String token = authorizationHeader.substring("Bearer ".length());
                        DecodedJWT decodedJWT = jwtUtil.decodedToken(token);
                        String username = decodedJWT.getSubject();
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                        if (jwtUtil.isTokenValid(username, token)) {
                            Collection<SimpleGrantedAuthority> authorities = jwtUtil.getAuthorities(roles);
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            filterChain.doFilter(request, response);
                        } else {
                            SecurityContextHolder.clearContext();
                        }

                    } catch (Exception exception) {
//                    log.error("Error logging in: {}", exception.getMessage());
                        response.setHeader("error", exception.getMessage());
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", exception.getMessage());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
                } else {
                    filterChain.doFilter(request, response);
                }
            }
        }

    }
}

