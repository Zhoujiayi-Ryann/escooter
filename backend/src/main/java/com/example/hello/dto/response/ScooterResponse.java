package com.example.hello.dto.response;

import com.example.hello.entity.Scooter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 滑板车响应DTO
 */
public class ScooterResponse {
    
    /**
     * 滑板车ID
     */
    private Integer scooter_id;
    
    /**
     * 纬度
     */
    private BigDecimal location_lat;
    
    /**
     * 经度
     */
    private BigDecimal location_lng;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 电池电量
     */
    private Integer battery_level;
    
    /**
     * 总使用时间（小时）
     */
    private Float total_usage_time;
    
    /**
     * 价格（每小时）
     */
    private BigDecimal price;
    
    /**
     * 最后维护日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime last_maintenance_date;
    
    /**
     * 最后使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime last_used_date;
    
    /**
     * 从实体类构建响应DTO
     * 
     * @param scooter 滑板车实体
     * @return 滑板车响应DTO
     */
    public static ScooterResponse fromEntity(Scooter scooter) {
        ScooterResponse response = new ScooterResponse();
        response.setScooter_id(scooter.getScooterId());
        response.setLocation_lat(scooter.getLocationLat());
        response.setLocation_lng(scooter.getLocationLng());
        response.setStatus(scooter.getStatus() != null ? scooter.getStatus().name() : "unknown");
        response.setBattery_level(scooter.getBatteryLevel());
        response.setTotal_usage_time(scooter.getTotalUsageTime());
        response.setPrice(scooter.getPrice());
        response.setLast_maintenance_date(scooter.getLastMaintenanceDate());
        response.setLast_used_date(scooter.getLastUsedDate());
        return response;
    }
    
    /**
     * 从实体列表构建响应DTO列表
     * 
     * @param scooters 滑板车实体列表
     * @return 滑板车响应DTO列表
     */
    public static List<ScooterResponse> fromEntities(List<Scooter> scooters) {
        return scooters.stream()
                .map(ScooterResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    // Getters and Setters
    public Integer getScooter_id() {
        return scooter_id;
    }
    
    public void setScooter_id(Integer scooter_id) {
        this.scooter_id = scooter_id;
    }
    
    public BigDecimal getLocation_lat() {
        return location_lat;
    }
    
    public void setLocation_lat(BigDecimal location_lat) {
        this.location_lat = location_lat;
    }
    
    public BigDecimal getLocation_lng() {
        return location_lng;
    }
    
    public void setLocation_lng(BigDecimal location_lng) {
        this.location_lng = location_lng;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getBattery_level() {
        return battery_level;
    }
    
    public void setBattery_level(Integer battery_level) {
        this.battery_level = battery_level;
    }
    
    public Float getTotal_usage_time() {
        return total_usage_time;
    }
    
    public void setTotal_usage_time(Float total_usage_time) {
        this.total_usage_time = total_usage_time;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public LocalDateTime getLast_maintenance_date() {
        return last_maintenance_date;
    }
    
    public void setLast_maintenance_date(LocalDateTime last_maintenance_date) {
        this.last_maintenance_date = last_maintenance_date;
    }
    
    public LocalDateTime getLast_used_date() {
        return last_used_date;
    }
    
    public void setLast_used_date(LocalDateTime last_used_date) {
        this.last_used_date = last_used_date;
    }
} 