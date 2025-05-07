package com.example.hello.controller;

import com.example.hello.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    // 图片存储根目录
    @Value("${app.upload.dir:${user.dir}/src/main/resources/static/feedback}")
    private String uploadDir;
    
    /**
     * 上传反馈图片
     * 
     * @param files 图片文件列表
     * @return 上传结果
     */
    @PostMapping("/feedback/images")
    public Result<List<String>> uploadFeedbackImages(@RequestParam("files") MultipartFile[] files) {
        logger.info("Received image upload request, file count: {}", files.length);
        
        if (files.length == 0) {
            return Result.error("No files selected");
        }
        
        try {
            List<String> uploadedUrls = new ArrayList<>();
            
            // 生成日期目录，格式为：yyyy-MM-dd
            String datePath = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            
            // 创建文件存储目录
            Path uploadPath = Paths.get(uploadDir, "images", datePath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created directory: {}", uploadPath);
            }
            
            // 处理每个文件
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                
                // 检查文件类型
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    logger.warn("File is not an image type: {}", file.getOriginalFilename());
                    continue;
                }
                
                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String filename = UUID.randomUUID().toString() + extension;
                
                // 存储文件
                Path filePath = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), filePath);
                logger.info("Saved file: {}", filePath);
                
                // 构建图片URL
                String imageUrl = "/api/feedback/images/" + datePath + "/" + filename;
                uploadedUrls.add(imageUrl);
            }
            
            logger.info("Image upload successful, URL list: {}", uploadedUrls);
            return Result.success(uploadedUrls, "Image upload successful");
            
        } catch (IOException e) {
            logger.error("Image upload failed", e);
            return Result.error("Image upload failed: " + e.getMessage());
        }
    }
} 