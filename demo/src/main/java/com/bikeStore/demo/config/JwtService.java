package com.bikeStore.demo.config;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${secret-key}")
    private String secretKey;

    @Value(("${token-expiration}"))
    private Long tokenExpiractionMs;

    @Value("${refresh-threshold}")
    private Long refreshThresholdMs;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private  String buildToken(Map<String, Object> claims, String subject, Long expiractionMs){

        return Jwts.builder()
                .setClaims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiractionMs))
                .signWith(getSigningKey())
                .compact();
    }

    private String generateToken(Long userId, Long rolId, String userName){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("UserName", userName);

        return buildToken(claims, userName, tokenExpiractionMs);
    }



    public boolean isTokenValid(String token){
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        }catch (JwtException e){
            e.printStackTrace();
            return false;
        }
    }



}
