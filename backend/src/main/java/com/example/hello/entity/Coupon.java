package com.example.hello.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("coupon_id")
    private Integer couponId;

    /**
     * 优惠券名称
     */
    @JsonProperty("coupon_name")
    private String couponName;

    /**
     * 最低消费金额
     */
    @JsonProperty("min_spend")
    private BigDecimal minSpend;

    /**
     * 优惠金额
     */
    @JsonProperty("coupon_amount")
    private BigDecimal couponAmount;

    /**
     * 有效期开始时间
     */
    @JsonProperty("valid_from")
    private LocalDate validFrom;

    /**
     * 有效期结束时间
     */
    @JsonProperty("valid_to")
    private LocalDate validTo;

    /**
     * 是否激活
     */
    @JsonProperty("is_active")
    private Boolean isActive;
    
    /**
     * 是否已使用（来自Users_Coupons表）
     */
    @JsonProperty("is_used")
    private Boolean isUsed;
    
    /**
     * 关联的订单ID（来自Users_Coupons表）
     */
    @JsonProperty("order_id")
    private Integer orderId;
    
    /**
     * 计算优惠券的状态（是否可用于当前订单）
     * 状态: "able" - 可用, "disable" - 不可用
     */
    private String status;
}