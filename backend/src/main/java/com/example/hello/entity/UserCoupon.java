package com.example.hello.entity;

import lombok.Data;

/**
 * 用户优惠券关联实体类
 * 对应数据库中的 Users_Coupons 表
 */
@Data
public class UserCoupon {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 优惠券ID
     */
    private Integer couponId;

    /**
     * 是否已使用
     */
    private Boolean isUsed;

    /**
     * 使用的订单ID
     */
    private Integer orderId;
}