package com.example.hello.controller;

import com.example.hello.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    
    // 默认上传路径，可在application.properties中配置
    private static final String DEFAULT_UPLOAD_DIR = "static/uploads";
    
    @Value("${file.upload.dir:#{null}}")
    private String configuredUploadDir;
    
    private final ResourceLoader resourceLoader;
    
    public UploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param module 模块名称，如feedback, order等
     * @return 上传结果，包含图片URL
     */
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "module", defaultValue = "feedback") String module) {
        
        if (file.isEmpty()) {
            return Result.error("上传失败：文件为空");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("上传失败：只允许上传图片文件");
        }
        
        // 检查文件大小（最大5MB）
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            return Result.error("上传失败：文件大小不能超过5MB");
        }
        
        try {
            // 获取上传目录
            String uploadDir = getUploadDir();
            
            // 按模块和日期组织目录结构
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateDir = dateFormat.format(new Date());
            String relativePath = String.format("%s/%s", module, dateDir);
            String fullUploadPath = String.format("%s/%s", uploadDir, relativePath);
            
            // 创建目录
            Path dirPath = Paths.get(fullUploadPath);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                logger.info("创建目录: {}", dirPath);
            }
            
            // 生成文件名
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown");
            String extension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = Paths.get(fullUploadPath, newFilename);
            Files.copy(file.getInputStream(), filePath);
            
            // 生成访问URL
            String fileUrl = String.format("/uploads/%s/%s", relativePath, newFilename);
            
            logger.info("文件上传成功: {}", fileUrl);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            
            return Result.success(result, "上传成功");
            
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取配置的上传目录
     */
    private String getUploadDir() {
        // 如果配置了上传目录，则使用配置的目录
        if (configuredUploadDir != null && !configuredUploadDir.isEmpty()) {
            return configuredUploadDir;
        }
        
        // 否则使用默认目录
        try {
            String resourcePath = resourceLoader.getResource("classpath:").getURI().getPath();
            return resourcePath + DEFAULT_UPLOAD_DIR;
        } catch (IOException e) {
            // 如果获取路径失败，返回一个可能的默认路径
            logger.warn("无法获取资源路径，使用相对路径", e);
            return DEFAULT_UPLOAD_DIR;
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename.lastIndexOf(".") > 0) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }
} 