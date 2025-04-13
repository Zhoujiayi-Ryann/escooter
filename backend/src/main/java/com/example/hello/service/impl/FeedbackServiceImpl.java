package com.example.hello.service.impl;

import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.response.FeedbackListResponse;
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
        logger.info("Start to submit feedback: {}", request);
        
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
        logger.info("Feedback record created, ID: {}", feedbackId);
        
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
                logger.warn("Cannot convert bill number to order ID: {}", request.getBillNumber());
            }
        }
        
        // 5. 构建并返回响应
        FeedbackResponse response = FeedbackResponse.of(
                feedbackId,
                orderId,
                Optional.ofNullable(imageUrls).orElse(Collections.emptyList())
        );
        
        logger.info("Feedback submitted successfully: {}", response);
        return response;
    }
    
    /**
     * 获取所有反馈记录（包含图片）
     * 使用Java 8 Stream API处理数据转换
     *
     * @return 反馈列表响应
     */
    @Override
    public List<FeedbackListResponse> getAllFeedback() {
        logger.info("Start to get all feedback information");
        
        try {
            // 查询所有反馈记录
            List<Feedback> feedbacks = feedbackMapper.findAllFeedback();
            
            // 转换为响应DTO
            List<FeedbackListResponse> responseList = feedbacks.stream()
                    .map(feedback -> {
                        try {
                            // 查询该反馈对应的所有图片
                            List<FeedbackImage> images = feedbackImageMapper.getFeedbackImagesById(feedback.getId());
                            
                            // 安全地获取枚举类型的名称，避免NullPointerException
                            String feedbackType = feedback.getFeedbackType() != null ? 
                                    feedback.getFeedbackType().name() : "UNKNOWN";
                            String status = feedback.getStatus() != null ? 
                                    feedback.getStatus().name() : "PENDING";
                            String priority = feedback.getPriority() != null ? 
                                    feedback.getPriority().name() : "MEDIUM";
                            
                            // 构建响应对象
                            FeedbackListResponse response = FeedbackListResponse.builder()
                                    .id(feedback.getId())
                                    .user_id(feedback.getUserId())
                                    .feedback_type(feedbackType)
                                    .description(feedback.getDescription())
                                    .status(status)
                                    .priority(priority)
                                    .happening_time(feedback.getHappeningTime() != null ? 
                                            feedback.getHappeningTime().atStartOfDay() : null)
                                    .bill_number(feedback.getBillNumber())
                                    .created_at(feedback.getCreatedAt())
                                    .build();
                            
                            // 转换图片列表
                            if (!CollectionUtils.isEmpty(images)) {
                                List<FeedbackListResponse.FeedbackImageDto> imageDtos = images.stream()
                                        .map(image -> FeedbackListResponse.FeedbackImageDto.builder()
                                                .url(image.getImageUrl())
                                                .upload_time(image.getUploadTime())
                                                .build())
                                        .collect(Collectors.toList());
                                response.setImages(imageDtos);
                            }
                            
                            return response;
                        } catch (Exception e) {
                            // 记录转换单个反馈时的错误，但继续处理其他反馈
                            logger.error("Error processing feedback record: feedbackId={}, error: {}", 
                                    feedback.getId(), e.getMessage());
                            
                            // 返回一个带有基本信息的响应对象
                            return FeedbackListResponse.builder()
                                    .id(feedback.getId())
                                    .user_id(feedback.getUserId())
                                    .description(feedback.getDescription() != null ? 
                                            feedback.getDescription() : "读取失败")
                                    .status("UNKNOWN")
                                    .priority("UNKNOWN")
                                    .created_at(feedback.getCreatedAt())
                                    .build();
                        }
                    })
                    .collect(Collectors.toList());
            
            logger.info("Successfully get all feedback information, total: {}", responseList.size());
            return responseList;
        } catch (Exception e) {
            logger.error("Failed to get all feedback information: {}", e.getMessage(), e);
            // 返回空列表而不是抛出异常，这样接口至少能返回200状态码
            return Collections.emptyList();
        }
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
        
        logger.info("Save feedback images, feedbackId: {}, imageUrls size: {}", feedbackId, imageUrls.size());
        
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
        logger.info("Successfully save {} feedback images", images.size());
    }
} 