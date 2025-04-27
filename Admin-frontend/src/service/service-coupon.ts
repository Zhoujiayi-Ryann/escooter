import { http } from '@/utils/http'
import type { Result } from '@/types/result'

/**
 * 优惠券管理相关接口
 */

// 优惠券分发请求参数类型
interface CouponDistributeRequest {
  coupon_id: number
  user_ids: number[]
}

// 优惠券分发响应类型
interface CouponDistributeResponse {
  success_count: number
  fail_count: number
}

// 优惠券基本信息类型
interface Coupon {
  coupon_id: number
  coupon_name: string
  min_spend: number
  coupon_amount: number
  valid_from: string
  valid_to: string
  is_active: boolean
}

/**
 * 获取所有优惠券列表
 */
export function getAllCoupons(): Promise<Result<Coupon[]>> {
  return http.get('/api/coupons')
}

/**
 * 添加新优惠券
 */
export function addCoupon(coupon: Omit<Coupon, 'coupon_id'>): Promise<Result<string>> {
  return http.post('/api/coupons', coupon)
}

/**
 * 批量发放优惠券
 */
export function distributeCoupons(data: CouponDistributeRequest): Promise<Result<CouponDistributeResponse>> {
  return http.post('/api/coupons/distribute', data)
}

/**
 * 停用优惠券（软删除）
 */
export function deactivateCoupon(couponId: number): Promise<Result<Coupon>> {
  return http.patch(`/api/coupons/${couponId}/deactivate`)
}

/**
 * 激活优惠券
 */
export function activateCoupon(couponId: number): Promise<Result<Coupon>> {
  return http.patch(`/api/coupons/${couponId}/activate`)
} 