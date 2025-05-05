package com.example.hello.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

/**
 * 延长订单请求DTO
 */
@Data
public class ExtendOrderRequest {
    /**
     * 订单ID
     */
    @NotNull(message = "Order ID cannot be empty")
    private Integer order_id;

    /**
     * 新的结束时间
     */
    @NotNull(message = "New end time cannot be empty")
    @Future(message = "New end time must be a future date")
    private LocalDateTime new_end_time;
}