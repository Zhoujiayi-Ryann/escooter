import request from '@/utils/request';

/**
 * 优惠券管理相关接口
 */

/**
 * 获取所有优惠券列表
 */
export const getAllCoupons = () => {
  return request({
    url: '/api/coupons',
    method: 'GET'
  });
};

/**
 * 添加新优惠券
 * @param data 优惠券信息
 */
export const addCoupon = (data: {
  coupon_name: string;
  min_spend: number;
  coupon_amount: number;
  valid_from: string;
  valid_to: string;
}) => {
  return request({
    url: '/api/coupons',
    method: 'POST',
    data
  });
};

/**
 * 批量发放优惠券
 * @param data 发放信息
 */
export const distributeCoupons = (data: {
  coupon_id: number;
  user_ids: number[];
}) => {
  return request({
    url: '/api/coupons/distribute',
    method: 'POST',
    data
  });
};

/**
 * 停用优惠券
 * @param couponId 优惠券ID
 */
export const deactivateCoupon = (couponId: number) => {
  return request({
    url: `/coupons/${couponId}/deactivate`,
    method: 'PATCH'
  });
};

/**
 * 激活优惠券
 * @param couponId 优惠券ID
 */
export const activateCoupon = (couponId: number) => {
  return request({
    url: `/coupons/${couponId}/activate`,
    method: 'PATCH'
  });
};

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