package com.example.hello.dto.request;

import com.example.hello.entity.Scooter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 滑板车请求DTO
 */
public class ScooterRequest {
    
    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private BigDecimal location_lat;
    
    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal location_lng;
    
    /**
     * 状态（添加时默认为free）
     */
    private String status;
    
    /**
     * 电池电量
     */
    @NotNull(message = "电池电量不能为空")
    @Min(value = 0, message = "电池电量最小为0")
    @Max(value = 100, message = "电池电量最大为100")
    private Integer battery_level;
    
    /**
     * 价格（每小时）
     */
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能为负数")
    private BigDecimal price;
    
    // Getters and Setters
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * 转换为滑板车实体类
     * 
     * @return 滑板车实体
     */
    public Scooter toEntity() {
        Scooter scooter = new Scooter();
        scooter.setLocationLat(this.location_lat);
        scooter.setLocationLng(this.location_lng);
        
        // 状态转换，如果状态为空则默认为free
        if (this.status != null && !this.status.isEmpty()) {
            try {
                scooter.setStatus(Scooter.Status.valueOf(this.status));
            } catch (IllegalArgumentException e) {
                // 如果提供的状态不是有效的枚举值，默认设置为free
                scooter.setStatus(Scooter.Status.free);
            }
        } else {
            scooter.setStatus(Scooter.Status.free);
        }
        
        scooter.setBatteryLevel(this.battery_level);
        scooter.setPrice(this.price);
        scooter.setTotalUsageTime(0.0f); // 新增滑板车，使用时间默认为0
        
        return scooter;
    }
    
    @Override
    public String toString() {
        return "ScooterRequest{" +
                "location_lat=" + location_lat +
                ", location_lng=" + location_lng +
                ", status='" + status + '\'' +
                ", battery_level=" + battery_level +
                ", price=" + price +
                '}';
    }
} 