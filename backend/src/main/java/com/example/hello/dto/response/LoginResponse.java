package com.example.hello.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer user_id;
    private String username;
    private String token;
} 