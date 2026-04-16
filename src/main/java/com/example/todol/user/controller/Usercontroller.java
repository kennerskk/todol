package com.example.todol.user.controller;

import com.example.todol.user.model.Usermodel;
import com.example.todol.user.service.Userservice;
import com.example.todol.dto.RegisterRequest;
import com.example.todol.dto.LoginRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class Usercontroller {

    @Autowired
    private Userservice userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usermodel> getAllUsers() {
        return userService.getAllUsers();
    }



    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        userService.register(req);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = userService.login(req); //สร้าง token จาก service
        ResponseCookie cookie = ResponseCookie.from("token", token) // สร้าง cookie
                .httpOnly(true)     //  กัน JS อ่าน 
                .secure(false)      // true ถ้าใช้ HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 1 วัน
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // ส่ง cookie กลับไปใน header
                .body("Login success");
    }

}