package com.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = System.getenv("JWT_SECRET");
    private static final String EXPIRATION_ENV = System.getenv("JWT_EXPIRATION");
    private static final long EXPIRATION = (EXPIRATION_ENV != null)
            ? Long.parseLong(EXPIRATION_ENV)
            : 1000 * 60 * 60 * 24; // default 24 hours

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
