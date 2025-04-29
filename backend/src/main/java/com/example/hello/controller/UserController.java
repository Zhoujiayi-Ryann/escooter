package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.dto.request.ChangePasswordRequest;
import com.example.hello.entity.User;
import com.example.hello.service.UserService;
import com.example.hello.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return Result.success(response, "Login successfully");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/profile/{id}")
    public Result<User> getUserProfile(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user == null) {
                return Result.error("User not found");
            }
            return Result.success(user, "Get user information successfully");
        } catch (Exception e) {
            return Result.error("Failed to get user information: " + e.getMessage());
        }
    }

    @PatchMapping("/profile/{id}")
    public Result<User> updateUserProfile(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        try {
            User updatedUser = userService.updateUser(id, request);
            return Result.success(updatedUser, "Update user information successfully");
        } catch (Exception e) {
            return Result.error("Failed to update user information: " + e.getMessage());
        }
    }

    /**
     * 修改用户密码
     * 
     * @param id      用户ID
     * @param request 修改密码请求
     * @return 更新后的用户信息
     */
    @PatchMapping("/profile/{id}/password")
    public Result<User> changePassword(@PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            User updatedUser = userService.changePassword(id, request);
            return Result.success(updatedUser, "Change password successfully");
        } catch (Exception e) {
            return Result.error("Failed to change password: " + e.getMessage());
        }
    }

    /**
     * 查询常用用户（使用时长超过50小时）
     */
    @GetMapping("/frequent")
    public Result<List<User>> getFrequentUsers() {
        try {
            List<User> users = userService.findFrequentUsers(50.0f);
            if (users != null) {
                // 处理敏感信息
                users.forEach(user -> {
                    user.setPassword(null); // 移除密码
                    user.setPhoneNumber(null); // 移除手机号
                    user.setEmail(null); // 移除邮箱
                });
                return Result.success(users, "Frequent users found");
            }
            return Result.error("No frequent users found");
        } catch (Exception e) {
            logger.error("Query frequent users failed", e);
            return Result.error("System error, query failed");
        }
    }
}