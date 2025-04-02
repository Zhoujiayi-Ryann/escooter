package com.example.hello.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.hello.common.OrderStatus;

/**
 * 订单实体类
 * 对应数据库中的 Orders 表
 */
@Data
public class Order {
    /**
     * 订单ID（自增主键）
     */
    private Integer orderId;

    /**
     * 用户ID（外键关联Users表）
     */
    private Integer userId;

    /**
     * 滑板车ID（外键关联Scooters表）
     */
    private Integer scooterId;

    /**
     * 订单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 订单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 订单持续时间（小时）
     */
    private Float duration;

    /**
     * 订单费用
     */
    private BigDecimal cost;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 延长时间（小时）
     */
    private Float extendedDuration;

    /**
     * 折扣金额
     */
    private BigDecimal discount;

    /**
     * 取车地址
     */
    private String address;

    /**
     * 是否已删除（伪删除）
     */
    private Boolean isDeleted;

    /**
     * 订单创建时间
     */
    private LocalDateTime createdAt;
}
