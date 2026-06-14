package com.example.todol.util;

import com.example.todol.dto.TokenData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    JwtUtil jwtUtil = new JwtUtil(
        "my-secret-key-that-is-at-least-32-bytes-long"
    );

    @AfterEach
    void tearDown() {
        // Clear security context after each test to avoid test pollution
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtil.generateToken(
            "test@example.com",
            "USER",
            "123"
        );

        TokenData data = jwtUtil.validateToken(token);
        assertNotNull(data);
        assertEquals("test@example.com", data.getEmail());
        assertEquals("123", data.getUserId());
        assertTrue(data.getRoles().contains("ROLE_USER"));
    }

    @Test
    void shouldGetCurrentUserIdFromToken() {
        TokenData tokenData = new TokenData("test@example.com", List.of("ROLE_USER"), "123");
        List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(tokenData, null, auths);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Retrieve userId from SecurityContextHolder via getCurrentUserIdFromToken
        String retrievedUserId = jwtUtil.getCurrentUserIdFromToken();
        assertEquals("123", retrievedUserId);
    }
}
