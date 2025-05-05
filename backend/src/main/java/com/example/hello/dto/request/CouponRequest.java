package com.example.hello.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * 优惠券请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequest {
    /**
     * 优惠券ID
     */
    @NotNull(message = "Coupon ID cannot be empty")
    private Integer couponId;
    
    /**
     * 订单ID
     */
    @NotNull(message = "Order ID cannot be empty")
    private Integer orderId;
} 