package com.example.hello.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 银行卡响应DTO
 * 用于返回银行卡基本信息
 */
@Data
public class CreditCardResponse {
    /**
     * 银行卡ID
     */
    private Integer card_id;
    
    /**
     * 掩码处理后的银行卡号
     * 只显示最后4位，其余用*代替
     */
    private String card_number;
    
    /**
     * 卡片到期日期
     * 格式为：yyyy-MM-dd
     */
    private LocalDate expiry_date;
    
    /**
     * 发卡国家/地区
     */
    private String country;
} 