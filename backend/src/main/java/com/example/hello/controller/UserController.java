package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.LoginRequest;
import com.example.hello.dto.response.LoginResponse;
import com.example.hello.dto.request.RegisterRequest;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.entity.User;
import com.example.hello.service.UserService;
import com.example.hello.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

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
    public Result<User> getUserProfile(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("token");
            // 解析token获取用户信息
            Claims claims = jwtUtil.parseToken(token);
            Integer tokenUserId = claims.get("userId", Integer.class);

            // 验证token中的用户ID与请求的ID是否匹配
            if (!id.equals(tokenUserId.longValue())) {
                return Result.error("No access to other user information");
            }

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
            @Valid @RequestBody UpdateUserRequest request,
            HttpServletRequest httpRequest) {
        try {
            // 从请求头获取token
            String token = httpRequest.getHeader("token");
            // 解析token获取用户信息
            Claims claims = jwtUtil.parseToken(token);
            Integer tokenUserId = claims.get("userId", Integer.class);

            // 验证token中的用户ID与请求的ID是否匹配
            if (!id.equals(tokenUserId.longValue())) {
                return Result.error("No access to modify other user information");
            }

            User updatedUser = userService.updateUser(id, request);
            return Result.success(updatedUser, "Update user information successfully");
        } catch (Exception e) {
            return Result.error("Failed to update user information: " + e.getMessage());
        }
    }
}