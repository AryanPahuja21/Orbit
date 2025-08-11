package com.orbit.user.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes); // Use decoded bytes to create key
        jwtParser = Jwts.parser().verifyWith((SecretKey) key).build();
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .subject(username)
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    public <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver) {
        final var claims = jwtParser.parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, io.jsonwebtoken.Claims::getExpiration);
        return expiration.before(new Date());
    }
}