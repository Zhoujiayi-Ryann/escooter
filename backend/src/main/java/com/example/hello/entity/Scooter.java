package com.example.hello.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Scooter {
    private Integer scooterId;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private String status;
    private Integer batteryLevel;
    private Float totalUsageTime;
    private BigDecimal price;
    private LocalDateTime lastMaintenanceDate;
    private LocalDateTime lastUsedDate;
} 