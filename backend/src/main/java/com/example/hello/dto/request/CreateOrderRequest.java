package com.example.hello.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * 创建订单请求DTO
 */
@Data
public class CreateOrderRequest {
    /**
     * 用户ID
     */
    @NotNull(message = "User ID cannot be empty")
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
    // @FutureOrPresent(message = "Start time must be the current or future time")
    private LocalDateTime start_time;
    
    /**
     * 结束时间
     */
    @NotNull(message = "End time cannot be empty")
    @FutureOrPresent(message = "End time must be the current or future time")
    private LocalDateTime end_time;
} 