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
         * 时长4小时到1天的订单收入
         */
        private BigDecimal fourHoursToOneDay;

        /**
         * 时长1天到1周的订单收入
         */
        private BigDecimal oneDayToOneWeek;

        /**
         * 时长大于1周的订单收入
         */
        private BigDecimal moreThanOneWeek;

        public BigDecimal getLessThanOneHour() {
            return lessThanOneHour;
        }

        public void setLessThanOneHour(BigDecimal lessThanOneHour) {
            this.lessThanOneHour = lessThanOneHour;
        }

        public BigDecimal getOneToFourHours() {
            return oneToFourHours;
        }

        public void setOneToFourHours(BigDecimal oneToFourHours) {
            this.oneToFourHours = oneToFourHours;
        }

        public BigDecimal getFourHoursToOneDay() {
            return fourHoursToOneDay;
        }

        public void setFourHoursToOneDay(BigDecimal fourHoursToOneDay) {
            this.fourHoursToOneDay = fourHoursToOneDay;
        }

        public BigDecimal getOneDayToOneWeek() {
            return oneDayToOneWeek;
        }

        public void setOneDayToOneWeek(BigDecimal oneDayToOneWeek) {
            this.oneDayToOneWeek = oneDayToOneWeek;
        }

        public BigDecimal getMoreThanOneWeek() {
            return moreThanOneWeek;
        }

        public void setMoreThanOneWeek(BigDecimal moreThanOneWeek) {
            this.moreThanOneWeek = moreThanOneWeek;
        }
    }
}