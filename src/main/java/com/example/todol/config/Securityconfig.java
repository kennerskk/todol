package com.example.todol.config;

import com.example.todol.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@Configuration //บอกให้ spring มาสแกนว่ามี bean มั้ย
@EnableMethodSecurity //เปิดใช้งาน method level security เช่น @PreAuthorize
public class Securityconfig {
    @Bean
    public PasswordEncoder passwordEncoder() {//อันนี้เอาไว้ hash password ก่อนเก็บลง database 
        return new BCryptPasswordEncoder();//งงดิว่าทำไมไม่ใช้ BCryptPasswordEncoder เลย Bcrypt เป็นแค่ algorithm ในอนาคตถ้าอยากเปลี่ยนก็แค่เปลี่ยน return แค่นั้นไม่ต้องไปแก้ที่อื่นๆ
    }
    @Bean
    //อันนี้เมจิคจัดก็คือ default ของ spring security จะถูกสร้างถ้ามันไม่มี SecurityFilterChain แต่เราดันสร้างไว้มันก็เลยไม่สร้าง default แล้วใช้ของเราที่เราสร้างเองแทน
    //ซึ่งหลักๆ spring security แบบ default จะบล็อกทุกอย่างไว้หมดเลย 
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {//อันนี้จำเป็นสูตรไปก่อนละกันงงๆอยู่
        http
            .csrf(csrf -> csrf.disable())//อันนี้คือ ถ้าใช้ csrf มันจะต้องมี token ทุกครั้งที่ส่ง request มา แต่เราจะใช้ jwt แทนเลย disable ไป
            .formLogin(httpForm -> httpForm.disable())//ปิด filter แบบ default UsernamePasswordAuthenticationFilter
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/login", "/api/users/register").permitAll() //อันนี้คือ อนุญาตให้เข้าถึง endpoint login กับ register 
                .anyRequest().authenticated() //อันอื่นต้อง authenticated ก่อนถึงจะเข้าถึงได้
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// บอกว่าใช้ jwtFilter ก่อน UsernamePasswordAuthenticationFilter ซึ่งเป็น filter ที่ spring security ใช้ในการตรวจสอบ username กับ password แบบ default

        return http.build();//นำเอาไปใช้
    }
}