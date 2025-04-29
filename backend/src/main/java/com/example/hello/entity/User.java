package com.example.hello.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 用户实体类
 * 对应数据库中的Users表
 */
@Data
public class User {
    /**
     * 用户ID，主键
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 用户注册日期
     */
    private LocalDateTime registrationDate;

    /**
     * 用户总使用时长（小时）
     */
    private Float totalUsageHours;

    /**
     * 用户总消费金额
     */
    private BigDecimal totalSpent;

    /**
     * 用户类型，用于区分不同类型的用户
     */
    private String userTypes;

    /**
     * 用户头像路径
     */
    private String avatarPath;

    /**
     * 用户是否被禁用，0-正常，1-禁用
     */
    private Boolean isDisabled;
}