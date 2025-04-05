package com.example.hello.dto;

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
    @NotNull(message = "订单ID不能为空")
    private Integer order_id;

    /**
     * 新的结束时间
     */
    @NotNull(message = "新的结束时间不能为空")
    @Future(message = "新的结束时间必须是将来时间")
    private LocalDateTime new_end_time;
}