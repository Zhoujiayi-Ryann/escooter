// 密码相关API
import { request } from '../request';
import { ApiResponse } from '../types';

/**
 * 密码API模块
 */
export const passwordApi = {
    /**
     * 发送忘记密码验证码
     * @param email 用户邮箱
     * @returns 发送结果
     */
    forgotPassword(email: string): Promise<ApiResponse<string>> {
        return request<string>({
            url: '/password/forgot',
            method: 'POST',
            data: { email }
        });
    },

    /**
     * 验证验证码
     * @param email 用户邮箱
     * @param code 验证码
     * @returns 验证结果
     */
    verifyCode(email: string, code: string): Promise<ApiResponse<string>> {
        return request<string>({
            url: '/password/verify-code',
            method: 'POST',
            data: { email, code }
        });
    },

    /**
     * 重置密码
     * @param email 用户邮箱
     * @param code 验证码
     * @param newPassword 新密码
     * @returns 重置结果
     */
    resetPassword(email: string, code: string, newPassword: string): Promise<ApiResponse<string>> {
        return request<string>({
            url: '/password/reset',
            method: 'POST',
            data: { email, code, newPassword }
        });
    }
}; 