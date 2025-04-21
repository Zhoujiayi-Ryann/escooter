package com.example.hello.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 支付时间
     */
    private LocalDateTime paid_at;
} 