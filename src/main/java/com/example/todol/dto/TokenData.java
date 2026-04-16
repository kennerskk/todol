package com.example.todol.dto;

import java.util.List;

public class TokenData {
    private String email;
    private List<String> roles;

    public TokenData(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

}