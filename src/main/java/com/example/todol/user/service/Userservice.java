package com.example.todol.user.service;

import com.example.todol.dto.LoginRequest;
import com.example.todol.dto.RegisterRequest;

import com.example.todol.user.model.Usermodel;

import com.example.todol.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.todol.user.repository.Userrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Userservice {
    @Autowired
    private Userrepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public List<Usermodel> getAllUsers() {
        return userRepository.findAll();
    }

    public Usermodel getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public Usermodel createUser(Usermodel user) {
        return userRepository.save(user);
    }

    public Usermodel updateUser(String id, Usermodel updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Usermodel user = new Usermodel();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setRole("USER"); // กำหนด role เป็น USER โดย default
        // hash password before saving
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequest req) {
        Usermodel user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());// สร้าง token โดยใช้ email และ role เป็นข้อมูลใน
                                                                      // token แล้ว controller จะเอา token นี้ไปใส่ใน
                                                                      // cookie แล้วส่งกลับไปให้ client
    }
}