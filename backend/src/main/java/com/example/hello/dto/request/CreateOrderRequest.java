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
    @NotNull(message = "用户ID不能为空")
    private Integer user_id;
    
    /**
     * 滑板车ID
     */
    @NotNull(message = "滑板车ID不能为空")
    private Integer scooter_id;
    
    /**
     * 取车地址
     */
    @NotBlank(message = "取车地址不能为空")
    private String pickup_address;
    
    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @FutureOrPresent(message = "开始时间必须是当前或将来的时间")
    private LocalDateTime start_time;
    
    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @FutureOrPresent(message = "结束时间必须是当前或将来的时间")
    private LocalDateTime end_time;
} 