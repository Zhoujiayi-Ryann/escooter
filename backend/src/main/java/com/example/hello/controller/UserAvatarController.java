package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.UpdateUserRequest;
import com.example.hello.entity.User;
import com.example.hello.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserAvatarController {
    private static final Logger logger = LoggerFactory.getLogger(UserAvatarController.class);
    
    @Autowired
    private UserService userService;
    
    @Value("${app.avatar.upload.dir}")
    private String uploadDir;
    
    @Value("${app.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${app.virtual-path.avatar}")
    private String avatarVirtualPath;
    
    @PostMapping("/avatar/upload/{userId}")
    public Result<String> uploadAvatar(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("Received avatar upload request for user: {}", userId);
        
        if (file.isEmpty()) {
            return Result.error("Please select a file");
        }
        
        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created directory: {}", uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String newFilename = "avatar_" + userId + "_" + UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);
            logger.info("Saved avatar file: {}", filePath);
            
            // 返回虚拟路径
            String relativePath = avatarVirtualPath + "/" + newFilename;
            String fullUrl = serverUrl + relativePath;
            
            // 更新数据库中的头像路径（存储相对路径）
            UpdateUserRequest updateRequest = new UpdateUserRequest();
            updateRequest.setAvatar_path(relativePath);
            userService.updateUser(userId, updateRequest);
            
            return Result.success(fullUrl, "Avatar upload successful");
            
        } catch (IOException e) {
            logger.error("Avatar upload failed", e);
            return Result.error("Avatar upload failed: " + e.getMessage());
        }
    }

    /**
     * 获取用户头像
     * @param userId 用户ID
     * @return 头像URL
     */
    @GetMapping("/avatar/{userId}")
    public Result<String> getAvatar(@PathVariable Long userId) {
        logger.info("Getting avatar for user: {}", userId);
        
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return Result.error("User not found");
            }
            
            String avatarPath = user.getAvatarPath();
            if (avatarPath == null || avatarPath.trim().isEmpty()) {
                return Result.success(serverUrl + "/static/settings/userp.jpg", "Default avatar");
            }
            
            // 如果头像路径是相对路径，添加服务器URL
            if (!avatarPath.startsWith("http")) {
                avatarPath = serverUrl + avatarPath;
            }
            
            return Result.success(avatarPath, "Avatar retrieved successfully");
        } catch (Exception e) {
            logger.error("Failed to get avatar for user: {}", userId, e);
            return Result.error("Failed to get avatar: " + e.getMessage());
        }
    }
} 