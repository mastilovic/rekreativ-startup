package com.example.rekreativ.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.rekreativ.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@Slf4j
//TODO: util class only has static methods, refactor name of the class
public class JwtUtil {

    private static final String SECRET_KEY = "jKZGcDKSY1fcmFAwxVsof5GicOdsz9sD";

    public DecodedJWT decodedToken(String token) {
        log.debug("calling decodeToken method");

        JWTVerifier verifier = tokenVerifier();

        return verifier.verify(token);
    }

    public boolean isTokenValid(String username, String token) {
        log.debug("calling isTokenValid method");

        JWTVerifier verifier = tokenVerifier();

        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        log.debug("calling isTokenExpired method");

        Date expiration = verifier.verify(token).getExpiresAt();

        return expiration.before(new Date());
    }

    public JWTVerifier tokenVerifier() {
        log.debug("calling tokenVerifier method");

        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY.getBytes());
            verifier = JWT.require(algorithm).withIssuer("issuer").build();

        } catch (JWTVerificationException e) {
            log.debug("Token cant be verified", e);

            throw new JWTVerificationException("Token cant be verified!");
        }

        return verifier;
    }

    public Authentication getAuthentication(String username,
                                            Collection<SimpleGrantedAuthority> authorities,
                                            HttpServletRequest request) {
        log.debug("calling getAuthentication method");

        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);

        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return userPasswordAuthToken;
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        log.debug("calling getAuthorities method");

        String[] claims = getClaimsFromToken(token);

        return stream(claims)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String generateJwtToken(AuthUser authUser) {
        log.debug("calling generateJwtToken method");

        String[] claims = getClaimsFromUser(authUser);

        return JWT.create().withIssuer("issuer").withAudience("Audience")
                .withIssuedAt(new Date()).withSubject(authUser.getUsername())
                .withArrayClaim("roles", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC512(SECRET_KEY.getBytes()));
    }

    public String getSubject(String token) {
        log.debug("calling getSubject method");

        JWTVerifier verifier = tokenVerifier();

        return verifier.verify(token).getSubject();
    }

    private String[] getClaimsFromToken(String token) {
        log.debug("calling getClaimsFromToken method");

        JWTVerifier verifier = tokenVerifier();

        return verifier.verify(token).getClaim("roles").asArray(String.class);
    }

    private String[] getClaimsFromUser(AuthUser user) {
        log.debug("calling getClaimsFromUser method");

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return authorities.toArray(new String[0]);
    }
}
