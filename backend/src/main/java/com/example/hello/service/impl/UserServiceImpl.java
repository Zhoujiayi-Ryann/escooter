package com.example.hello.service.impl;

import com.example.hello.dto.LoginRequest;
import com.example.hello.dto.LoginResponse;
import com.example.hello.dto.RegisterRequest;
import com.example.hello.dto.UpdateUserRequest;
import com.example.hello.entity.User;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.UserService;
import com.example.hello.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.checkUsernameExists(request.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.checkEmailExists(request.getEmail()) > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 密码加密
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone_number());
        user.setRegistrationDate(LocalDateTime.now());
        user.setTotalUsageHours(0.0f);
        user.setTotalSpent(BigDecimal.ZERO);

        // 保存用户
        userMapper.insertUser(user);

        // 清除密码后返回
        user.setPassword(null);
        return user;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());

        // 构建响应对象
        LoginResponse response = new LoginResponse();
        response.setUser_id(user.getUserId());
        response.setUsername(user.getUsername());
        response.setToken(token);

        return response;
    }

    @Override
    public User getUserById(Long id) {
        User user = userMapper.findById(id);
        if (user != null) {
            // 出于安全考虑，清除密码信息
            user.setPassword(null);
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        // 获取现有用户信息
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户名（如果提供）
        if (request.getUsername() != null && !request.getUsername().equals(existingUser.getUsername())) {
            // 检查新用户名是否已存在
            if (userMapper.checkUsernameExists(request.getUsername()) > 0) {
                throw new RuntimeException("用户名已存在");
            }
            existingUser.setUsername(request.getUsername());
        }

        // 更新密码（如果提供）
        if (request.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 更新邮箱（如果提供）
        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            // 检查新邮箱是否已存在
            if (userMapper.checkEmailExists(request.getEmail()) > 0) {
                throw new RuntimeException("邮箱已被注册");
            }
            existingUser.setEmail(request.getEmail());
        }

        // 更新手机号（如果提供）
        if (request.getPhone_number() != null) {
            existingUser.setPhoneNumber(request.getPhone_number());
        }

        // 更新用户信息
        userMapper.updateUser(existingUser);

        // 清除密码后返回
        existingUser.setPassword(null);
        return existingUser;
    }
}