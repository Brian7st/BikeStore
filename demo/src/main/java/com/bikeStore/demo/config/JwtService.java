package com.bikeStore.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtService {

    @Value("${secret-key}")
    private String secretKey;

    @Value(("${token-expiration}"))
    private Long tokenExpiractionMs;

    @Value("${refresh-threshold}")
    private Long refreshThresholdMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private String buildToken(Map<String, Object> claims, String subject, Long expiractionMs) {

        return Jwts.builder()
                .setClaims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiractionMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(String userName, String roleName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", Collections.singletonList("ROLE_" + roleName));

        return buildToken(claims, userName, tokenExpiractionMs);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUserName(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object roles = claims.get("roles");
            if (roles instanceof List) {
                return (List<String>) roles;
            }
            return Collections.emptyList();
        });

    }
}


