package com.example.hello.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class RevenueStatisticsResponse {
    /**
     * 时间段内的总收入
     */
    private BigDecimal totalRevenue;

    /**
     * 每天的收入统计
     * key: 日期 (yyyy-MM-dd)
     * value: 该日期的总收入
     */
    private Map<String, BigDecimal> dailyRevenue;

    /**
     * 每天不同时长的订单收入统计
     * key: 日期 (yyyy-MM-dd)
     * value: 该日期不同时长的收入统计
     */
    private Map<String, DurationRevenue> dailyDurationRevenue;

    @Data
    public static class DurationRevenue {
        /**
         * 时长小于1小时的订单收入
         */
        private BigDecimal lessThanOneHour;

        /**
         * 时长1-4小时的订单收入
         */
        private BigDecimal oneToFourHours;

        /**
         * 时长大于4小时的订单收入
         */
        private BigDecimal moreThanFourHours;
    }
}