package com.example.hello.service;

import com.example.hello.entity.Coupon;
import com.example.hello.dto.response.CouponDistributeResponse;
import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {
    
    /**
     * 获取所有优惠券
     */
    List<Coupon> getAllCoupons();

    /**
     * 添加新优惠券
     */
    boolean addCoupon(Coupon coupon);

    /**
     * 批量发放优惠券
     * @param couponId 优惠券ID
     * @param userIds 用户ID列表
     * @return 发放结果
     */
    CouponDistributeResponse distributeCoupons(Integer couponId, List<Integer> userIds);

    /**
     * 停用优惠券
     * @param couponId 优惠券ID
     * @return 更新后的优惠券信息
     */
    Coupon deactivateCoupon(Integer couponId);

    /**
     * 激活优惠券
     * @param couponId 优惠券ID
     * @return 更新后的优惠券信息
     */
    Coupon activateCoupon(Integer couponId);
} 