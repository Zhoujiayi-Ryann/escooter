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
     * 新的结束时间（用于延长订单）
     */
    private LocalDateTime new_end_time;

    /**
     * 延长时间（小时）
     */
    private Float extended_duration;

    /**
     * 延长订单的额外费用
     */
    private BigDecimal extended_cost;

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
    
    /**
     * 订单创建时间
     */
    private LocalDateTime created_at;

    /**
     * 订单是否删除: 0代表未删除，1代表已删除
     */
    private Integer is_deleted;
}