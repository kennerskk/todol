package com.example.todol.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data; 
@Data
@AllArgsConstructor
public class TokenData {
    private String email;
    private List<String> roles;
    private String userId;
}