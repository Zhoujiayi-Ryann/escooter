package com.example.hello.dto.response;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 支付订单响应DTO
 * 用于返回订单支付结果
 */
@Data
public class PayOrderResponse {
    /**
     * 订单ID
     */
    private Integer order_id;
    
    /**
     * 用户ID
     */
    private Integer user_id;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 订单费用（含折扣后的最终金额）
     */
    private BigDecimal cost;
    
    /**
     * 使用的优惠券ID
     */
    private Integer coupon_id;
    
    /**
     * 优惠券优惠金额
     */
    private BigDecimal coupon_amount;
} 