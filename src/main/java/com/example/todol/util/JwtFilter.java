package com.example.todol.util;

import com.example.todol.util.JwtUtil;
import com.example.todol.dto.TokenData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;// private เพื่อป้องกันไม่ให้คลาสอื่นเข้าถึงได้ final
                                  // คือตัวแปรนี้จะถูกกำหนดค่าเพียงครั้งเดียว

    public JwtFilter(JwtUtil jwtUtil) { // ระบุให้ bean นี้มี jwtUtil เป็นส่วนหนึ่งตอนสร้าง
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, // อันนี้เป็นการเขียนทับ method เดิมของ
                                                                // OncePerRequestFilter
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;
        // ทำการดึง uri จาก request มาเช็คว่าเป็น endpoint ไหน ถ้าเป็น login กับ
        // register ก็ไม่ต้องเช็ค token
        String uri = request.getRequestURI();
        if (uri.equals("/api/users/login") || uri.equals("/api/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            TokenData tokenData = jwtUtil.validateToken(token);

            if (tokenData != null) {

                List<GrantedAuthority> auths = tokenData.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(tokenData,
                        null, auths);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}