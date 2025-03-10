package com.example.hello.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private LocalDateTime registrationDate;
    private Float totalUsageHours;
    private BigDecimal totalSpent;
    private String userTypes;
} 