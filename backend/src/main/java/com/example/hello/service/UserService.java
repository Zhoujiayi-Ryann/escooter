package com.example.hello.service;

import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.entity.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    User getUserById(Long id);

    User updateUser(Long id, UpdateUserRequest request);

    /**
     * 查询常用用户（使用时长超过指定小时数）
     * @param minHours 最小使用小时数
     * @return 符合条件的用户列表
     */
    List<User> findFrequentUsers(float minHours);
}