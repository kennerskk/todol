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
@RequestMapping("/api/users") // หากมีรีเควสเข้ามาให้ดูว่าเริ่มต้นด้วย api/users
public class Usercontroller {

    @Autowired
    private Userservice userService;// นำ bean userService มาใช้งาน

    @GetMapping // รีเควสที่เป็น "/" จะมาที่นี้
    @PreAuthorize("hasRole('ADMIN')") // ตรวจสอบว่า user มี role เป็น admin หรือไม่
    public List<Usermodel> getAllUsers() {
        return userService.getAllUsers();// return ข้อมูลผู้ใช้ทั้งหมด
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        userService.register(req);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = userService.login(req); // สร้าง token จาก service
        ResponseCookie cookie = ResponseCookie.from("token", token) // สร้าง cookie
                .httpOnly(true) // กัน JS อ่าน
                .secure(false) // true ถ้าใช้ HTTPS
                .path("/")
                .maxAge(24 * 60 * 60) // 1 วัน
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // ส่ง cookie กลับไปใน header
                .body("Login success");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "") // เคลียร์ token ออก
                .httpOnly(true)
                .secure(false) // ตั้งเป็น true ถ้าใช้ HTTPS
                .path("/")
                .maxAge(0) // กำหนดอายุการใช้งานเป็น 0 เพื่อลบออกทันที
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // ส่ง cookie กลับไปเขียนทับตัวเดิม
                .body("Logout success");
    }

}