package com.orbit.user.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        key = Keys.hmacShaKeyFor(keyBytes);
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
        try {
            final var claims = jwtParser.parseSignedClaims(token).getPayload();
            log.info("✅ JWT parsed successfully!");
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("❌ JWT parsing failed: {}", e.getMessage(), e);
        }
        return null;
    }


    public boolean isTokenValid(String token, String userId) {
        String extractUserId = extractClaim(token, claims -> claims.get("userId", String.class));
        return (extractUserId.equals(userId)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, io.jsonwebtoken.Claims::getExpiration);
        return expiration.before(new Date());
    }
}