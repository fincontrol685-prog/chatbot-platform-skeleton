package com.br.chatbotplatformskeleton.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final Key key;
    private final long validityMs;

    public JwtUtil(@Value("${security.jwt.secret:thisissecuredefaultjwtsecretkeyyoucanchange}") String secret,
                   @Value("${security.jwt.expiration-ms:900000}") long validityMs) {
        // Ensure the secret is at least 256 bits (32 bytes) for HS256
        String finalSecret = secret.length() < 32 ? padSecret(secret) : secret;
        this.key = Keys.hmacShaKeyFor(finalSecret.getBytes());
        this.validityMs = validityMs;
    }

    /**
     * Pads a secret to at least 256 bits (32 bytes) for HMAC-SHA256
     */
    private String padSecret(String secret) {
        if (secret.length() >= 32) {
            return secret;
        }
        StringBuilder padded = new StringBuilder(secret);
        while (padded.length() < 32) {
            padded.append("0");
        }
        return padded.toString();
    }

    public String generateToken(String subject, List<String> roles) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
