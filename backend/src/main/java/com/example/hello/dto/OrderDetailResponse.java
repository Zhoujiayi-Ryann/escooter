package com.example.hello.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情响应DTO
 * 包含订单基本信息和关联的滑板车信息
 */
@Data
public class OrderDetailResponse {
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
     * 延长时间（小时）
     */
    private Float extended_duration;

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
    private ScooterInfoDto scooter_info;

    /**
     * 内部类：滑板车信息DTO
     */
    @Data
    public static class ScooterInfoDto {
        /**
         * 纬度
         */
        private BigDecimal latitude;

        /**
         * 经度
         */
        private BigDecimal longitude;

        /**
         * 电池电量
         */
        private Integer battery_level;

        /**
         * 价格
         */
        private BigDecimal price;
    }
}