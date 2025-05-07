import axios from 'axios';

// API基础URL
const BASE_URL = 'http://172.20.10.2:8080/api';

// 响应类型接口
interface ApiResponse<T> {
  code: number;
  data: T;
  msg: string;
}

/**
 * 优惠券管理相关接口
 */

// 优惠券请求接口
export interface CouponRequest {
  coupon_name: string;
  min_spend: number;
  coupon_amount: number;
  valid_from: string;
  valid_to: string;
}

// 优惠券分发请求接口
export interface CouponDistributeRequest {
  coupon_id: number;
  user_ids: number[];
}

// 优惠券接口返回数据类型定义
export interface ICoupon {
  coupon_id: number;
  coupon_name: string;
  min_spend: number;
  coupon_amount: number;
  valid_from: string;
  valid_to: string;
  is_active: boolean;
}

export interface IDistributeResponse {
  success_count: number;
  fail_count: number;
}

// 优惠券服务类
export const couponService = {
  /**
   * 获取所有优惠券列表
   */
  async getAllCoupons(): Promise<ICoupon[]> {
    try {
      const response = await axios.get<ApiResponse<ICoupon[]>>(`${BASE_URL}/coupons`);

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return [];
    } catch (error) {
      console.error('获取优惠券列表失败:', error);
      return [];
    }
  },

  /**
   * 添加新优惠券
   * @param couponData 优惠券信息
   */
  async addCoupon(couponData: CouponRequest): Promise<boolean> {
    try {
      const response = await axios.post<ApiResponse<any>>(`${BASE_URL}/coupons`, couponData);

      return response.data.code === 1;
    } catch (error) {
      console.error('添加优惠券失败:', error);
      return false;
    }
  },

  /**
   * 批量发放优惠券
   * @param data 发放信息
   */
  async distributeCoupons(data: CouponDistributeRequest): Promise<IDistributeResponse | null> {
    try {
      const response = await axios.post<ApiResponse<IDistributeResponse>>(
        `${BASE_URL}/coupons/distribute`,
        data
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error('发放优惠券失败:', error);
      return null;
    }
  },

  /**
   * 停用优惠券
   * @param couponId 优惠券ID
   */
  async deactivateCoupon(couponId: number): Promise<ICoupon | null> {
    try {
      const response = await axios.patch<ApiResponse<ICoupon>>(
        `${BASE_URL}/coupons/${couponId}/deactivate`
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`停用优惠券(ID: ${couponId})失败:`, error);
      return null;
    }
  },

  /**
   * 激活优惠券
   * @param couponId 优惠券ID
   */
  async activateCoupon(couponId: number): Promise<ICoupon | null> {
    try {
      const response = await axios.patch<ApiResponse<ICoupon>>(
        `${BASE_URL}/coupons/${couponId}/activate`
      );

      if (response.data.code === 1 && response.data.data) {
        return response.data.data;
      }
      return null;
    } catch (error) {
      console.error(`激活优惠券(ID: ${couponId})失败:`, error);
      return null;
    }
  }
}; 