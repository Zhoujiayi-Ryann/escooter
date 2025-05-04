package com.example.hello.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理员为未注册用户创建订单的请求DTO
 */
@Data
public class AdminTempUserOrderRequest {
    /**
     * 用户ID（实际为管理员ID）
     */
    @NotNull(message = "Admin ID cannot be empty")
    private Integer user_id;
    
    /**
     * 滑板车ID
     */
    @NotNull(message = "Scooter ID cannot be empty")
    private Integer scooter_id;
    
    /**
     * 取车地址
     */
    @NotBlank(message = "Pickup address cannot be empty")
    private String pickup_address;
    
    /**
     * 开始时间
     */
    @NotNull(message = "Start time cannot be empty")
    @FutureOrPresent(message = "Start time must be the current or future time")
    private LocalDateTime start_time;
    
    /**
     * 结束时间
     */
    @NotNull(message = "End time cannot be empty")
    @FutureOrPresent(message = "End time must be the current or future time")
    private LocalDateTime end_time;
    
    /**
     * 订单金额（由管理员手动输入）
     */
    @NotNull(message = "Order amount cannot be empty")
    @DecimalMin(value = "0.0", inclusive = true, message = "Order amount cannot be negative")
    private BigDecimal cost;
} 