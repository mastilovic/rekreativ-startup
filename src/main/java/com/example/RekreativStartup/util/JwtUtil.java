package com.example.RekreativStartup.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.RekreativStartup.auth.AuthUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.stream;

@Component
//@Slf4j
public class JwtUtil {

    private static final String SECRET_KEY = "jKZGcDKSY1fcmFAwxVsof5GicOdsz9sD";

    //decode token
    public DecodedJWT decodedToken(String token){
        JWTVerifier verifier = tokenVerifier();
        DecodedJWT decoded = verifier.verify(token);

        return decoded;
    }

    //validate token
    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = tokenVerifier();

        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token);
    }

    //check if token is expired
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();

        return expiration.before(new Date());
    }

    //verify token
    public JWTVerifier tokenVerifier(){
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY.getBytes());
            verifier = JWT.require(algorithm).withIssuer("issuer").build();
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token cant be verified!");
        }

        return verifier;
    }

    //get authorities from token
    public Collection<SimpleGrantedAuthority> getAuthorities(String[] roles){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return authorities;
    }

    //generate JWT token
    public String generateJwtToken(AuthUserDetails authUserDetails) {
        String[] claims = getClaimsFromUser(authUserDetails);

        return JWT.create().withIssuer("issuer").withAudience("Audience")
                .withIssuedAt(new Date()).withSubject(authUserDetails.getUsername())
                .withArrayClaim("roles", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7* 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC512(SECRET_KEY.getBytes()));
    }

    private String[] getClaimsFromToken(String token){
        JWTVerifier verifier = tokenVerifier();
        return verifier.verify(token).getClaim("authorities").asArray(String.class);
    }

    //get claims from user
    private String[] getClaimsFromUser(AuthUserDetails user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }

        return authorities.toArray(new String[0]);
    }

}
