package com.example.hello.service;

import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.dto.request.ChangePasswordRequest;
import com.example.hello.entity.User;

import java.util.List;

public interface UserService {
    User register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse adminLogin(LoginRequest request);

    User getUserById(Long id);

    User updateUser(Long id, UpdateUserRequest request);

    /**
     * 修改用户密码
     * 
     * @param id      用户ID
     * @param request 修改密码请求
     * @return 更新后的用户信息
     */
    User changePassword(Long id, ChangePasswordRequest request);

    /**
     * 查询常用用户（使用时长超过指定小时数）
     * 
     * @param minHours 最小使用小时数
     * @return 符合条件的用户列表
     */
    List<User> findFrequentUsers(float minHours);

    /**
     * 获取所有非管理员用户
     * 
     * @return 非管理员用户列表
     */
    List<User> findAllNonAdminUsers();

    /**
     * 切换用户禁用状态
     * 
     * @param userId 用户ID
     * @return 更新后的用户信息
     */
    User toggleUserDisabledStatus(Long userId);
}