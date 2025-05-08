package com.example.hello.service.impl;

import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.dto.request.ChangePasswordRequest;
import com.example.hello.entity.User;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.UserService;
import com.example.hello.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.server.url:https://khnrsggvzudb.sealoshzh.site}")
    private String serverUrl;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.checkUsernameExists(request.getUsername()) > 0) {
            throw new RuntimeException("Username already exists");
        }

        // 检查邮箱是否已存在
        if (userMapper.checkEmailExists(request.getEmail()) > 0) {
            throw new RuntimeException("Email already exists");
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
            throw new RuntimeException("Username or password is incorrect");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or password is incorrect");
        }
        
        // 验证账号是否被禁用
        if (user.getIsDisabled() != null && user.getIsDisabled()) {
            logger.warn("User {} attempted to login but account is disabled", user.getUsername());
            throw new RuntimeException("Account has been disabled");
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
    public LoginResponse adminLogin(LoginRequest request) {
        // 根据用户名查询用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("Username or password is incorrect");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username or password is incorrect");
        }
        
        // 验证账号是否被禁用
        if (user.getIsDisabled() != null && user.getIsDisabled()) {
            logger.warn("Admin {} attempted to login but account is disabled", user.getUsername());
            throw new RuntimeException("Account has been disabled");
        }

        // 验证是否为管理员
        if (user.getUserTypes() == null || !user.getUserTypes().contains("admin")) {
            throw new RuntimeException("User is not an administrator");
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
            // 如果头像路径是相对路径，添加服务器URL
            if (user.getAvatarPath() != null && !user.getAvatarPath().startsWith("http")) {
                user.setAvatarPath(serverUrl + user.getAvatarPath());
            }
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        // 获取现有用户信息
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // 更新用户名（如果提供）
        if (request.getUsername() != null && !request.getUsername().equals(existingUser.getUsername())) {
            // 检查新用户名是否已存在
            if (userMapper.checkUsernameExists(request.getUsername()) > 0) {
                throw new RuntimeException("Username already exists");
            }
            existingUser.setUsername(request.getUsername());
        }

        // 更新邮箱（如果提供）
        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            // 检查新邮箱是否已存在
            if (userMapper.checkEmailExists(request.getEmail()) > 0) {
                throw new RuntimeException("Email already exists");
            }
            existingUser.setEmail(request.getEmail());
        }

        // 更新手机号（如果提供）
        if (request.getPhone_number() != null) {
            existingUser.setPhoneNumber(request.getPhone_number());
        }

        // 更新头像路径（如果提供）
        if (request.getAvatar_path() != null) {
            existingUser.setAvatarPath(request.getAvatar_path());
        }

        // 更新用户信息
        userMapper.updateUser(existingUser);

        // 清除密码后返回
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    @Transactional
    public User changePassword(Long id, ChangePasswordRequest request) {
        // 获取现有用户信息
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // 验证原密码是否正确
        if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // 更新密码
        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 更新用户信息
        userMapper.updateUser(existingUser);

        // 清除密码后返回
        existingUser.setPassword(null);
        return existingUser;
    }

    @Override
    public List<User> findFrequentUsers(float minHours) {
        logger.info("开始查询使用时长超过 {} 小时的常用用户", minHours);
        try {
            List<User> users = userMapper.findFrequentUsers(minHours);
            logger.info("查询到 {} 个常用用户", users.size());
            return users;
        } catch (Exception e) {
            logger.error("查询常用用户时发生错误: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> findAllNonAdminUsers() {
        List<User> users = userMapper.findAllNonAdminUsers();
        // 出于安全考虑，清除所有用户的密码信息
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    @Override
    @Transactional
    public User toggleUserDisabledStatus(Long id) {
        // 获取现有用户信息
        User existingUser = userMapper.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // 切换禁用状态
        Boolean newStatus = !existingUser.getIsDisabled();
        userMapper.updateUserDisabledStatus(id.intValue(), newStatus);
        existingUser.setIsDisabled(newStatus);

        // 清除密码后返回
        existingUser.setPassword(null);
        return existingUser;
    }
}