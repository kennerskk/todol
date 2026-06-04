package com.example.todol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ดักจับ RuntimeException ทั้งหมดที่เราโยนใน Service (เช่น อีเมลซ้ำ, รหัสผ่านผิด)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
    }

    // ดักจับข้อผิดพลาดทั่วไปอื่น ๆ ที่อาจจะเกิดขึ้นแบบไม่คาดคิด (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An unexpected error occurred: " + e.getMessage()));
    }
}
