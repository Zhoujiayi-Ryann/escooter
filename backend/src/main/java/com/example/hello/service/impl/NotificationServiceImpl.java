package com.example.hello.service.impl;

import com.example.hello.entity.Notification;
import com.example.hello.mapper.NotificationMapper;
import com.example.hello.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知服务实现类
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public boolean createNotification(Notification notification) {
        logger.info("Creating notification: {}", notification);
        try {
            if (notification == null) {
                logger.error("Notification object is null");
                return false;
            }
            
            // 设置默认值
            if (notification.getIsRead() == null) {
                notification.setIsRead(false);
            }
            
            // 设置创建时间为中国时区
            if (notification.getCreatedAt() == null) {
                notification.setCreatedAt(LocalDateTime.now(CHINA_ZONE));
            }
            
            int result = notificationMapper.createNotification(notification);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error creating notification: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int batchCreateNotifications(List<Notification> notifications) {
        logger.info("Batch creating {} notifications", notifications != null ? notifications.size() : 0);
        try {
            if (notifications == null || notifications.isEmpty()) {
                logger.error("Notifications list is null or empty");
                return 0;
            }
            
            // 设置默认值和创建时间
            LocalDateTime now = LocalDateTime.now(CHINA_ZONE);
            notifications.forEach(notification -> {
                if (notification.getIsRead() == null) {
                    notification.setIsRead(false);
                }
                if (notification.getCreatedAt() == null) {
                    notification.setCreatedAt(now);
                }
            });
            
            return notificationMapper.batchCreateNotifications(notifications);
        } catch (Exception e) {
            logger.error("Error batch creating notifications: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean createCouponNotification(Long userId, Integer couponId, String couponName) {
        logger.info("Creating coupon notification for user: {}, coupon: {}", userId, couponId);
        try {
            Notification notification = Notification.builder()
                    .userId(userId)
                    .type(Notification.NotificationType.COUPON)
                    .title("New Coupon Received")
                    .content("You received a new coupon: " + couponName)
                    .isRead(false)
                    .relatedId(couponId.longValue())
                    .createdAt(LocalDateTime.now(CHINA_ZONE))
                    .build();
            
            return createNotification(notification);
        } catch (Exception e) {
            logger.error("Error creating coupon notification: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int batchCreateCouponNotifications(List<Long> userIds, Integer couponId, String couponName) {
        logger.info("Batch creating coupon notifications for {} users, coupon: {}", userIds != null ? userIds.size() : 0, couponId);
        try {
            if (userIds == null || userIds.isEmpty()) {
                logger.error("User IDs list is null or empty");
                return 0;
            }
            
            LocalDateTime now = LocalDateTime.now(CHINA_ZONE);
            List<Notification> notifications = userIds.stream()
                    .map(userId -> Notification.builder()
                            .userId(userId)
                            .type(Notification.NotificationType.COUPON)
                            .title("New Coupon Received")
                            .content("You received a new coupon: " + couponName)
                            .isRead(false)
                            .relatedId(couponId.longValue())
                            .createdAt(now)
                            .build())
                    .collect(Collectors.toList());
            
            return batchCreateNotifications(notifications);
        } catch (Exception e) {
            logger.error("Error batch creating coupon notifications: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean createCommentReplyNotification(Long userId, Long feedbackId, String replyContent) {
        logger.info("Creating comment reply notification for user: {}, feedback: {}", userId, feedbackId);
        try {
            // 创建评论回复通知
            Notification notification = Notification.builder()
                    .userId(userId)
                    .type(Notification.NotificationType.COMMENT_REPLY)
                    .title("New Reply to Feedback")
                    .content("Your feedback has a new reply: " + (replyContent.length() > 50 ? replyContent.substring(0, 47) + "..." : replyContent))
                    .isRead(false)
                    .relatedId(feedbackId)
                    .createdAt(LocalDateTime.now(CHINA_ZONE))
                    .build();
            
            return createNotification(notification);
        } catch (Exception e) {
            logger.error("Error creating comment reply notification: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        logger.info("Getting notifications for user: {}", userId);
        try {
            return notificationMapper.getUserNotifications(userId);
        } catch (Exception e) {
            logger.error("Error getting user notifications: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Notification> getUserUnreadNotifications(Long userId) {
        logger.info("Getting unread notifications for user: {}", userId);
        try {
            return notificationMapper.getUserUnreadNotifications(userId);
        } catch (Exception e) {
            logger.error("Error getting user unread notifications: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public int countUserUnreadNotifications(Long userId) {
        logger.info("Counting unread notifications for user: {}", userId);
        try {
            return notificationMapper.countUserUnreadNotifications(userId);
        } catch (Exception e) {
            logger.error("Error counting user unread notifications: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean markAsRead(Long notificationId, Long userId) {
        logger.info("Marking notification as read: {}, user: {}", notificationId, userId);
        try {
            int result = notificationMapper.markAsRead(notificationId, userId);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public int markAllAsRead(Long userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        try {
            return notificationMapper.markAllAsRead(userId);
        } catch (Exception e) {
            logger.error("Error marking all notifications as read: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean deleteNotification(Long notificationId, Long userId) {
        logger.info("Deleting notification: {}, user: {}", notificationId, userId);
        try {
            int result = notificationMapper.deleteNotification(notificationId, userId);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error deleting notification: {}", e.getMessage(), e);
            return false;
        }
    }
} 