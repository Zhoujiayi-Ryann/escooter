package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 反馈列表响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackListResponse {
    
    /**
     * 反馈ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long user_id;
    
    /**
     * 反馈类型
     */
    private String feedback_type;
    
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime happening_time;
    
    /**
     * 相关账单号
     */
    private String bill_number;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime created_at;
    
    /**
     * 管理员回复内容
     */
    private String admin_reply;
    
    /**
     * 回复管理员ID
     */
    private Long reply_admin_id;
    
    /**
     * 管理员回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime replied_at;
    
    /**
     * 关联图片列表
     */
    @Builder.Default
    private List<FeedbackImageDto> images = new ArrayList<>();
    
    /**
     * 反馈图片DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackImageDto {
        /**
         * 图片URL
         */
        private String url;
        
        /**
         * 上传时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime upload_time;
    }
} 