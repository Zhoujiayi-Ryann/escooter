package com.example.hello.service;

import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.entity.User;

public interface UserService {
    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    User getUserById(Long id);

    User updateUser(Long id, UpdateUserRequest request);
}