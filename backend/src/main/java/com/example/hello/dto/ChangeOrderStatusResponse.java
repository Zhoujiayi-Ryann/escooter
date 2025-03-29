package com.example.hello.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单状态变更响应DTO
 * 用于返回订单状态变更的结果
 */
@Data
public class ChangeOrderStatusResponse {
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
     * 取车地址
     */
    private String pickup_address;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 滑板车信息
     */
    private OrderDetailResponse.ScooterInfoDto scooter_info;

    /**
     * 滑板车状态
     */
    private String scooter_status;
}