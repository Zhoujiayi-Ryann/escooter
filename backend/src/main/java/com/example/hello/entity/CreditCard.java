package com.example.hello.entity;

import lombok.Data;
import java.time.LocalDate;

/**
 * 银行卡实体类
 * 对应数据库中的 CreditCards 表
 */
@Data
public class CreditCard {
    /**
     * 银行卡ID（自增主键）
     */
    private Integer cardId;
    
    /**
     * 用户ID（外键关联Users表）
     */
    private Integer userId;
    
    /**
     * 银行卡号
     */
    private String cardNumber;
    
    /**
     * 安全码
     */
    private String securityCode;
    
    /**
     * 卡片到期日期
     */
    private LocalDate expiryDate;
    
    /**
     * 发卡国家/地区
     */
    private String country;
} 