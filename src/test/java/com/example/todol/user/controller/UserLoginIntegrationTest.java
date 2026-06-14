package com.example.todol.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.todol.dto.LoginRequest;
import com.example.todol.dto.RegisterRequest;
import com.example.todol.user.repository.Userrepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Userrepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testUserRegistrationAndLoginFlow() throws Exception {
        // 1. Register a new user
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setName("Test User");
        registerReq.setEmail("testuser@example.com");
        registerReq.setPassword("myPassword123");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk());

        // 2. Login with correct credentials
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail("testuser@example.com");
        loginReq.setPassword("myPassword123");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login success")))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().httpOnly("token", true))
                .andExpect(cookie().maxAge("token", 24 * 60 * 60));

        // 3. Login with incorrect credentials
        LoginRequest badLoginReq = new LoginRequest();
        badLoginReq.setEmail("testuser@example.com");
        badLoginReq.setPassword("wrongPassword");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badLoginReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }
}
