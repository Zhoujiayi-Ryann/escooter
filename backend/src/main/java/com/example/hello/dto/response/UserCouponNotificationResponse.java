package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户优惠券通知响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponNotificationResponse {
    
    /**
     * 通知ID
     */
    @JsonProperty("notification_id")
    private Long notificationId;
    
    /**
     * 优惠券ID
     */
    @JsonProperty("coupon_id")
    private Long couponId;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 是否已读
     */
    @JsonProperty("is_read")
    private Boolean isRead;
    
    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
} 