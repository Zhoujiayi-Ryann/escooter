package com.example.hello.controller;

import com.example.hello.entity.Notification;
import com.example.hello.service.NotificationService;
import com.example.hello.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理控制器
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取用户的所有通知
     */
    @GetMapping("/user/{userId}")
    public Result<List<Notification>> getUserNotifications(@PathVariable("userId") Long userId) {
        logger.info("Received request to get all notifications for user: {}", userId);
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return Result.success(notifications);
        } catch (Exception e) {
            logger.error("Failed to get notifications for user: {}", userId, e);
            return Result.error("Failed to get notifications");
        }
    }

    /**
     * 获取用户的未读通知
     */
    @GetMapping("/user/{userId}/unread")
    public Result<List<Notification>> getUserUnreadNotifications(@PathVariable("userId") Long userId) {
        logger.info("Received request to get unread notifications for user: {}", userId);
        try {
            List<Notification> notifications = notificationService.getUserUnreadNotifications(userId);
            return Result.success(notifications);
        } catch (Exception e) {
            logger.error("Failed to get unread notifications for user: {}", userId, e);
            return Result.error("Failed to get unread notifications");
        }
    }

    /**
     * 获取用户的未读通知数量
     */
    @GetMapping("/user/{userId}/unread/count")
    public Result<Integer> countUserUnreadNotifications(@PathVariable("userId") Long userId) {
        logger.info("Received request to count unread notifications for user: {}", userId);
        try {
            int count = notificationService.countUserUnreadNotifications(userId);
            return Result.success(count);
        } catch (Exception e) {
            logger.error("Failed to count unread notifications for user: {}", userId, e);
            return Result.error("Failed to count unread notifications");
        }
    }

    /**
     * 标记通知为已读
     */
    @PatchMapping("/{notificationId}/read")
    public Result<Boolean> markAsRead(
            @PathVariable("notificationId") Long notificationId,
            @RequestParam("userId") Long userId) {
        logger.info("Received request to mark notification as read: {}, user: {}", notificationId, userId);
        try {
            boolean success = notificationService.markAsRead(notificationId, userId);
            if (success) {
                return Result.success(true, "Notification marked as read");
            } else {
                return Result.error("Failed to mark notification as read");
            }
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}, user: {}", notificationId, userId, e);
            return Result.error("Error marking notification as read");
        }
    }

    /**
     * 标记所有通知为已读
     */
    @PatchMapping("/user/{userId}/read/all")
    public Result<Integer> markAllAsRead(@PathVariable("userId") Long userId) {
        logger.info("Received request to mark all notifications as read for user: {}", userId);
        try {
            int count = notificationService.markAllAsRead(userId);
            return Result.success(count, count + " notifications marked as read");
        } catch (Exception e) {
            logger.error("Error marking all notifications as read for user: {}", userId, e);
            return Result.error("Error marking all notifications as read");
        }
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{notificationId}")
    public Result<Boolean> deleteNotification(
            @PathVariable("notificationId") Long notificationId,
            @RequestParam("userId") Long userId) {
        logger.info("Received request to delete notification: {}, user: {}", notificationId, userId);
        try {
            boolean success = notificationService.deleteNotification(notificationId, userId);
            if (success) {
                return Result.success(true, "Notification deleted successfully");
            } else {
                return Result.error("Failed to delete notification");
            }
        } catch (Exception e) {
            logger.error("Error deleting notification: {}, user: {}", notificationId, userId, e);
            return Result.error("Error deleting notification");
        }
    }
} 