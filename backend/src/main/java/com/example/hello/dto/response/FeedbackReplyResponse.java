package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 反馈回复响应DTO
 */
@Data
@Builder
public class FeedbackReplyResponse {

    /**
     * 反馈ID
     */
    @JsonProperty("feedback_id")
    private Long feedbackId;

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * 管理员ID
     */
    @JsonProperty("admin_id")
    private Long adminId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复时间
     */
    @JsonProperty("reply_time")
    private LocalDateTime replyTime;

    /**
     * 反馈状态
     */
    private String status;

    /**
     * 是否已发送通知
     */
    @JsonProperty("notification_sent")
    private boolean notificationSent;
} 