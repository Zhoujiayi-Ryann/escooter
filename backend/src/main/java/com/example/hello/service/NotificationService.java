package com.example.hello.service;

import com.example.hello.entity.Notification;
import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {
    
    /**
     * 创建单个通知
     *
     * @param notification 通知对象
     * @return 是否创建成功
     */
    boolean createNotification(Notification notification);
    
    /**
     * 批量创建通知
     *
     * @param notifications 通知对象列表
     * @return 成功创建的通知数量
     */
    int batchCreateNotifications(List<Notification> notifications);
    
    /**
     * 为用户创建优惠券发放通知
     *
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @param couponName 优惠券名称
     * @return 是否创建成功
     */
    boolean createCouponNotification(Long userId, Integer couponId, String couponName);
    
    /**
     * 为多个用户创建优惠券发放通知
     *
     * @param userIds 用户ID列表
     * @param couponId 优惠券ID
     * @param couponName 优惠券名称
     * @return 成功创建的通知数量
     */
    int batchCreateCouponNotifications(List<Long> userIds, Integer couponId, String couponName);
    
    /**
     * 获取用户的所有通知
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getUserNotifications(Long userId);
    
    /**
     * 获取用户的未读通知
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    List<Notification> getUserUnreadNotifications(Long userId);
    
    /**
     * 获取用户的未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    int countUserUnreadNotifications(Long userId);
    
    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否标记成功
     */
    boolean markAsRead(Long notificationId, Long userId);
    
    /**
     * 标记用户所有通知为已读
     *
     * @param userId 用户ID
     * @return 标记为已读的通知数量
     */
    int markAllAsRead(Long userId);
    
    /**
     * 删除通知
     *
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteNotification(Long notificationId, Long userId);
} 