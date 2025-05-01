package com.example.hello.mapper;

import com.example.hello.entity.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 通知数据访问接口
 */
@Mapper
public interface NotificationMapper {

    /**
     * 创建通知
     */
    @Insert("INSERT INTO notifications (user_id, type, title, content, is_read, related_id) " +
            "VALUES (#{userId}, #{type}, #{title}, #{content}, #{isRead}, #{relatedId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createNotification(Notification notification);

    /**
     * 批量创建通知
     */
    @Insert("<script>" +
            "INSERT INTO notifications (user_id, type, title, content, is_read, related_id) VALUES " +
            "<foreach collection='notifications' item='notification' separator=','>" +
            "(#{notification.userId}, #{notification.type}, #{notification.title}, " +
            "#{notification.content}, #{notification.isRead}, #{notification.relatedId})" +
            "</foreach>" +
            "</script>")
    int batchCreateNotifications(@Param("notifications") List<Notification> notifications);

    /**
     * 获取用户的所有通知
     */
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "type", column = "type"),
        @Result(property = "title", column = "title"),
        @Result(property = "content", column = "content"),
        @Result(property = "isRead", column = "is_read"),
        @Result(property = "relatedId", column = "related_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Notification> getUserNotifications(Long userId);

    /**
     * 获取用户的未读通知
     */
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} AND is_read = 0 ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "type", column = "type"),
        @Result(property = "title", column = "title"),
        @Result(property = "content", column = "content"),
        @Result(property = "isRead", column = "is_read"),
        @Result(property = "relatedId", column = "related_id"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Notification> getUserUnreadNotifications(Long userId);

    /**
     * 获取用户的未读通知数量
     */
    @Select("SELECT COUNT(*) FROM notifications WHERE user_id = #{userId} AND is_read = 0")
    int countUserUnreadNotifications(Long userId);

    /**
     * 标记通知为已读
     */
    @Update("UPDATE notifications SET is_read = 1 WHERE id = #{notificationId} AND user_id = #{userId}")
    int markAsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    /**
     * 标记所有通知为已读
     */
    @Update("UPDATE notifications SET is_read = 1 WHERE user_id = #{userId} AND is_read = 0")
    int markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    @Delete("DELETE FROM notifications WHERE id = #{notificationId} AND user_id = #{userId}")
    int deleteNotification(@Param("notificationId") Long notificationId, @Param("userId") Long userId);
} 