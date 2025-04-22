package com.example.hello.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 优惠券实体类
 * 对应数据库中的 Coupons 表
 */
@Data
public class Coupon {
    /**
     * 优惠券ID
     */
    private Integer couponId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 最低消费金额
     */
    private BigDecimal minSpend;

    /**
     * 优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 有效期开始时间
     */
    private LocalDate validFrom;

    /**
     * 有效期结束时间
     */
    private LocalDate validTo;

    /**
     * 是否激活
     */
    private Boolean isActive;
    
    /**
     * 是否已使用（来自Users_Coupons表）
     */
    private Boolean isUsed;
    
    /**
     * 关联的订单ID（来自Users_Coupons表）
     */
    private Integer orderId;
    
    /**
     * 计算优惠券的状态（是否可用于当前订单）
     * 状态: "able" - 可用, "disable" - 不可用
     */
    private String status;
}