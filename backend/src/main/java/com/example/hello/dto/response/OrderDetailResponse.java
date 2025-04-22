package com.example.hello.dto.response;

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
     * 租赁天数
     */
    private Integer rental_days;

    /**
     * 租赁持续时间（小时）
     */
    private Float duration;

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
     * 新的结束时间（用于延长订单）
     */
    private LocalDateTime new_end_time;

    /**
     * 之前的状态（用于延长订单）
     */
    private String previous_status;

    /**
     * 是否已删除
     */
    private Boolean is_deleted;

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
         * 滑板车型号/样式
         */
        private String style;
        
        /**
         * 滑板车编号
         */
        private String number;
        
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
         * 单价（每小时）
         */
        private BigDecimal price;
    }
}