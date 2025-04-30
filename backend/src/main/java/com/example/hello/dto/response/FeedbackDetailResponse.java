package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 反馈详情响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetailResponse {
    /**
     * 反馈ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Long userId;
    
    /**
     * 反馈类型
     */
    @JsonProperty("feedback_type")
    private String feedbackType;
    
    /**
     * 反馈描述
     */
    private String description;
    
    /**
     * 处理状态
     */
    private String status;
    
    /**
     * 处理优先级
     */
    private String priority;
    
    /**
     * 问题发生时间
     */
    @JsonProperty("happening_time")
    private LocalDateTime happeningTime;
    
    /**
     * 相关账单号
     */
    @JsonProperty("bill_number")
    private String billNumber;
    
    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 图片URL列表
     */
    private List<String> images;
    
    /**
     * 用户信息
     */
    @JsonProperty("user_info")
    private UserInfo userInfo;
    
    /**
     * 用户信息DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String username;
        private String avatar;
    }
} 