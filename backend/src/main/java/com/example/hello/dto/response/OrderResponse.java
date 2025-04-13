package com.example.hello.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单响应DTO
 */
@Data
public class OrderResponse {
    /**
     * 订单ID
     */
    private Integer order_id;

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 滑板车ID
     */
    private Integer scooter_id;

    /**
     * 开始时间
     */
    private LocalDateTime start_time;

    /**
     * 结束时间
     */
    private LocalDateTime end_time;

    /**
     * 订单费用
     */
    private BigDecimal cost;
    
    /**
     * 折扣金额
     */
    private BigDecimal discount_amount;

    /**
     * 取车地址
     */
    private String pickup_address;

    /**
     * 订单状态
     */
    private String status;
}