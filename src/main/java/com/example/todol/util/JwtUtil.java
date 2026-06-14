package com.example.todol.util;

import com.example.todol.dto.TokenData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.security.Key;
import java.util.Date;

@Component // อันนี้คือการทำเป็น bean เพื่อให้สามารถ @Autowired ไปใช้ในที่อื่นได้
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;

    public JwtUtil() {
        // Default constructor for Spring Boot dependency injection
    }

    public JwtUtil(String secretKey) {
        this.secretKey = secretKey;
        this.expiration = 3600000; // Default 1 hour for testing
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email, String role, String userId) {// สร้าง token โดยรับ email และ role
                                                                           // เป็นข้อมูลใน token
        return Jwts.builder()
                .claim("role", "ROLE_" + role)
                .claim("userId", userId)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        // หน้าตาของ return token:
        // eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9BRE1JTiIsInN1YmplY3QiOiJ0ZXN0QGFkbWluLmNvbSIsImlhdCI6MTc0NDQ1MjExOCwiZXhwIjoxNzQ0NTM4NTE4fQ.d00t_R1mU2qW61gC0l9u3fP-gC41y9e8L9h7n6t5r4s
    }

    public TokenData validateToken(String token) {// ตัวนี้จะใช้ในการเช็คว่า token ถูกต้องไหม
        try {
            Claims claims = Jwts.parserBuilder()// parser มีหน้าที่ในการแกะ token และนำข้อมูลออกมา
                    .setSigningKey(getKey())// build() ไม่ใช่ method ของ setSigningKey แต่เป็น method ของ
                                            // parserBuilder() ที่ส่ง setSigningKey() เข้าไปแล้ว
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String userId = claims.get("userId", String.class);
            List<String> roles = role != null ? java.util.List.of(role) : java.util.Collections.emptyList();
            return new TokenData(email, roles, userId);
        } catch (Exception e) {
            return null;
        }
    }

    public String getCurrentUserIdFromToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        TokenData user = (TokenData) auth.getPrincipal();

        return user.getUserId();
    }

}
