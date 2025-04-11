package com.example.hello.service.impl;

import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.response.FeedbackResponse;
import com.example.hello.entity.Feedback;
import com.example.hello.entity.FeedbackImage;
import com.example.hello.mapper.FeedbackImageMapper;
import com.example.hello.mapper.FeedbackMapper;
import com.example.hello.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 反馈服务实现类
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    @Autowired
    private FeedbackImageMapper feedbackImageMapper;
    
    /**
     * 提交反馈
     * 使用事务确保反馈和图片同时成功或失败
     *
     * @param request 反馈请求
     * @return 反馈响应
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        logger.info("开始处理反馈提交请求: {}", request);
        
        // 1. 构建反馈实体
        Feedback feedback = new Feedback();
        feedback.setUserId(request.getUserId());
        feedback.setFeedbackType(request.getFeedbackType());
        feedback.setDescription(request.getDescription());
        feedback.setStatus(Feedback.Status.PENDING); // 默认状态为待处理
        feedback.setPriority(Feedback.Priority.MEDIUM); // 默认优先级为中
        feedback.setHappeningTime(request.getHappeningTime());
        feedback.setBillNumber(request.getBillNumber());
        feedback.setCreatedAt(LocalDateTime.now());
        
        // 2. 插入反馈记录
        feedbackMapper.insertFeedback(feedback);
        Long feedbackId = feedback.getId();
        logger.info("反馈记录已创建，ID: {}", feedbackId);
        
        // 3. 处理图片（如果有）
        List<String> imageUrls = request.getImageUrls();
        if (!CollectionUtils.isEmpty(imageUrls)) {
            saveImages(feedbackId, imageUrls);
        }
        
        // 4. 查找相关订单ID（如果有账单号）
        Long orderId = null;
        if (request.getBillNumber() != null) {
            // 注意：这里假设billNumber就是订单ID，实际项目中可能需要查询订单表
            try {
                orderId = Long.valueOf(request.getBillNumber());
            } catch (NumberFormatException e) {
                logger.warn("无法将账单号转换为订单ID: {}", request.getBillNumber());
            }
        }
        
        // 5. 构建并返回响应
        FeedbackResponse response = FeedbackResponse.of(
                feedbackId,
                orderId,
                Optional.ofNullable(imageUrls).orElse(Collections.emptyList())
        );
        
        logger.info("反馈提交处理完成: {}", response);
        return response;
    }
    
    /**
     * 保存反馈图片
     *
     * @param feedbackId 反馈ID
     * @param imageUrls 图片URL列表
     */
    private void saveImages(Long feedbackId, List<String> imageUrls) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            return;
        }
        
        logger.info("保存反馈图片，反馈ID: {}, 图片数量: {}", feedbackId, imageUrls.size());
        
        List<FeedbackImage> images = imageUrls.stream()
                .map(url -> {
                    FeedbackImage image = new FeedbackImage();
                    image.setFeedbackId(feedbackId);
                    image.setImageUrl(url);
                    image.setUploadTime(LocalDateTime.now());
                    return image;
                })
                .collect(Collectors.toList());
        
        feedbackImageMapper.batchInsertImages(images);
        logger.info("已保存 {} 张反馈图片", images.size());
    }
} 