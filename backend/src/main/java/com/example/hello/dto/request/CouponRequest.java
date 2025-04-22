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
    @NotNull(message = "优惠券ID不能为空")
    private Integer couponId;
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Integer orderId;
} 