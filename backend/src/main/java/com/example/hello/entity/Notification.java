package com.example.hello.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 通知实体类
 * 对应数据库中的notifications表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    /**
     * 通知ID
     */
    private Long id;
    
    /**
     * 接收用户ID
     */
    private Long userId;
    
    /**
     * 通知类型
     */
    private NotificationType type;
    
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
    private Boolean isRead;
    
    /**
     * 关联业务ID
     */
    private Long relatedId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 通知类型枚举
     */
    public enum NotificationType {
        COUPON,      // 优惠券通知
        COMMENT_REPLY // 评论回复通知
    }
} 