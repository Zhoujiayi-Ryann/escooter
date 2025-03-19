package com.example.hello.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class Order {
    private Integer orderId;
    private Integer userId;
    private Integer scooterId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private BigDecimal cost;
    private OrderStatus status; // 订单状态
    private Float extendedDuration; // 延长使用时间
    private String address; // 使用地址
    private BigDecimal discount;
}
