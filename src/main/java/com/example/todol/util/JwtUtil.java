package com.example.todol.util;

import com.example.todol.dto.TokenData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.security.Key;
import java.util.Date;

@Component//อันนี้คือการทำเป็น bean เพื่อให้สามารถ @Autowired ไปใช้ในที่อื่นได้
public class JwtUtil{
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long expiration;
    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email, String role) {//สร้าง token โดยรับ email และ role เป็นข้อมูลใน token
        return Jwts.builder()
                .claim("role", "ROLE_" + role) 
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenData validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            List<String> roles = role != null ? java.util.List.of(role) : java.util.Collections.emptyList();
            return new TokenData(email, roles);
        } catch (Exception e) {
            return null;
        }
    }

}
