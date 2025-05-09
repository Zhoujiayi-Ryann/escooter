import { request } from '../request';
import { ApiResponse, Coupon } from '../types';

/**
 * 优惠券API模块
 */
export const couponApi = {
    /**
     * 获取用户可用的优惠券列表
     * @param userId 用户ID
     * @returns 优惠券列表
     */
    getAvailableCoupons(userId: number): Promise<ApiResponse<Coupon[]>> {
        return request<Coupon[]>({
            url: `/users/${userId}/coupons`,
            method: 'GET',
            needToken: true
        });
    }
}; 