package com.raphaelcollin.usermanagement.infrastructure.token;

import com.raphaelcollin.usermanagement.core.TokenManager;
import com.raphaelcollin.usermanagement.core.User;
import com.raphaelcollin.usermanagement.core.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenManager implements TokenManager {
    private final String secret;
    private final Long tokenDurationInMilliseconds;

    public JwtTokenManager(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.duration}") Long tokenDurationInMilliseconds) {
        this.secret = secret;
        this.tokenDurationInMilliseconds = tokenDurationInMilliseconds;
    }

    @Override
    public String generateTokenForUser(final User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("type", user.getType());

        return Jwts
                .builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenDurationInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
    }

    @Override
    public User extractUserFromToken(final String token) {
        Jwt jwt;
        try {
            jwt = Jwts
                    .parser()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parse(token);
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }

        Claims claims = (Claims) jwt.getBody();
        String id = claims.get("id", String.class);
        String name = claims.get("name", String.class);
        String type = claims.get("type", String.class);
        String email = claims.get("email", String.class);

        return User.builder().
                id(id).
                name(name).
                email(email).
                type(type).
                build();
    }
}
