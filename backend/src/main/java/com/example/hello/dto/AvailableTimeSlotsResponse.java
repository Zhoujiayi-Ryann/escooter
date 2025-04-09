package com.example.hello.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 可用时间段响应DTO
 */
@Data
public class AvailableTimeSlotsResponse {
    /**
     * 当前订单的结束时间
     */
    private LocalDateTime current_end_time;

    /**
     * 下一个订单的开始时间
     */
    private LocalDateTime next_start_time;

    /**
     * 最大可延长时间（小时）
     */
    private Float max_extended_hours;
}