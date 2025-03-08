package com.example.hello.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer user_id;
    private String username;
    private String token;
} 