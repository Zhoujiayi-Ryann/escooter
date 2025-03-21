package com.example.hello.service;

import com.example.hello.dto.LoginRequest;
import com.example.hello.dto.LoginResponse;
import com.example.hello.dto.RegisterRequest;
import com.example.hello.entity.User;

public interface UserService {
    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    User getUserById(Long id);
}