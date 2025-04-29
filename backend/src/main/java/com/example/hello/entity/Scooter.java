package com.example.hello.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 滑板车实体类
 */
public class Scooter {
    
    /**
     * 滑板车状态枚举
     */
    public enum Status {
        /** 空闲可用 */
        free,
        /** 已预订 */
        booked,
        /** 使用中 */
        in_use,
        /** 维护中 */
        maintenance,
        /** 充电中 */
        charging

    }
    
    /** 滑板车ID */
    private Integer scooterId;
    
    /** 纬度 */
    private BigDecimal locationLat;
    
    /** 经度 */
    private BigDecimal locationLng;
    
    /** 滑板车状态 */
    private Status status;
    
    /** 电池电量 */
    private Integer batteryLevel;
    
    /** 总使用时间（小时） */
    private Float totalUsageTime;
    
    /** 价格（每小时） */
    private BigDecimal price;
    
    /** 最后维护日期 */
    private LocalDateTime lastMaintenanceDate;
    
    /** 最后使用日期 */
    private LocalDateTime lastUsedDate;
    
    /** 是否已删除（0-未删除，1-已删除） */
    private Integer isDeleted = 0;
    
    // Getters and Setters
    public Integer getScooterId() {
        return scooterId;
    }
    
    public void setScooterId(Integer scooterId) {
        this.scooterId = scooterId;
    }
    
    public BigDecimal getLocationLat() {
        return locationLat;
    }
    
    public void setLocationLat(BigDecimal locationLat) {
        this.locationLat = locationLat;
    }
    
    public BigDecimal getLocationLng() {
        return locationLng;
    }
    
    public void setLocationLng(BigDecimal locationLng) {
        this.locationLng = locationLng;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    
    public Float getTotalUsageTime() {
        return totalUsageTime;
    }
    
    public void setTotalUsageTime(Float totalUsageTime) {
        this.totalUsageTime = totalUsageTime;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }
    
    public void setLastMaintenanceDate(LocalDateTime lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }
    
    public LocalDateTime getLastUsedDate() {
        return lastUsedDate;
    }
    
    public void setLastUsedDate(LocalDateTime lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @Override
    public String toString() {
        return "Scooter{" +
                "scooterId=" + scooterId +
                ", locationLat=" + locationLat +
                ", locationLng=" + locationLng +
                ", status=" + status +
                ", batteryLevel=" + batteryLevel +
                ", totalUsageTime=" + totalUsageTime +
                ", price=" + price +
                ", lastMaintenanceDate=" + lastMaintenanceDate +
                ", lastUsedDate=" + lastUsedDate +
                ", isDeleted=" + isDeleted +
                '}';
    }
} 