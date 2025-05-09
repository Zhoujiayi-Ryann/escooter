// 通知相关API
import { request } from '../request';
import { ApiResponse, NotificationResponse } from '../types';

/**
 * 通知API模块
 */
export const notificationApi = {
    /**
     * 获取用户的所有通知
     * @param userId 用户ID
     * @returns 通知列表
     */
    getUserNotifications(userId: number): Promise<ApiResponse<NotificationResponse[]>> {
        return request<NotificationResponse[]>({
            url: `/notifications/user/${userId}`,
            method: 'GET',
            needToken: true
        });
    },

    /**
     * 获取用户的未读通知
     * @param userId 用户ID
     * @returns 未读通知列表
     */
    getUserUnreadNotifications(userId: number): Promise<ApiResponse<NotificationResponse[]>> {
        return request<NotificationResponse[]>({
            url: `/notifications/user/${userId}/unread`,
            method: 'GET',
            needToken: true
        });
    },

    /**
     * 获取用户的未读通知数量
     * @param userId 用户ID
     * @returns 未读通知数量
     */
    countUserUnreadNotifications(userId: number): Promise<ApiResponse<number>> {
        return request<number>({
            url: `/notifications/user/${userId}/unread/count`,
            method: 'GET',
            needToken: true
        });
    },

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @returns 是否标记成功
     */
    markAsRead(notificationId: number, userId: number): Promise<ApiResponse<boolean>> {
        return request<boolean>({
            url: `/notifications/${notificationId}/read`,
            method: 'PATCH',
            data: { userId },
            needToken: true
        });
    },

    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     * @returns 标记为已读的通知数量
     */
    markAllAsRead(userId: number): Promise<ApiResponse<number>> {
        return request<number>({
            url: `/notifications/user/${userId}/read/all`,
            method: 'PATCH',
            needToken: true
        });
    },

    /**
     * 删除通知
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @returns 是否删除成功
     */
    deleteNotification(notificationId: number, userId: number): Promise<ApiResponse<boolean>> {
        return request<boolean>({
            url: `/notifications/${notificationId}?userId=${userId}`,
            method: 'DELETE',
            needToken: true
        });
    }
};

/**
 * 使用示例：
 * 
 * import { notificationApi } from '../../utils/api/notification';
 * import { userApi } from '../../utils/api/user';
 * 
 * // 获取当前用户ID
 * const userId = userApi.getUserId();
 * 
 * // 获取用户未读通知
 * notificationApi.getUserUnreadNotifications(userId).then(res => {
 *     if (res.code === 1) {
 *         const notifications = res.data;
 *         console.log('未读通知:', notifications);
 *     }
 * });
 * 
 * // 获取未读通知数量（用于显示角标）
 * notificationApi.countUserUnreadNotifications(userId).then(res => {
 *     if (res.code === 1) {
 *         const count = res.data;
 *         console.log('未读通知数量:', count);
 *     }
 * });
 * 
 * // 标记通知为已读
 * notificationApi.markAsRead(notificationId, userId).then(res => {
 *     if (res.code === 1 && res.data) {
 *         console.log('通知已标记为已读');
 *     }
 * });
 * 
 * // 标记所有通知为已读
 * notificationApi.markAllAsRead(userId).then(res => {
 *     if (res.code === 1) {
 *         console.log(`${res.data}条通知已标记为已读`);
 *     }
 * });
 * 
 * // 删除通知
 * notificationApi.deleteNotification(notificationId, userId).then(res => {
 *     if (res.code === 1 && res.data) {
 *         console.log('通知已删除');
 *     }
 * });
 */ 