package com.example.hello.dto;

import lombok.Data;

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
     * 订单状态
     */
    private String status;
} 