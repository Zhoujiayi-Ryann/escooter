package com.example.hello.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 添加银行卡请求DTO
 */
@Data
public class AddCreditCardRequest {
    /**
     * 用户ID
     */
    @NotNull(message = "User ID cannot be empty")
    private Integer user_id;
    
    /**
     * 银行卡号
     * 通常为16位数字
     */
    @NotBlank(message = "Card number cannot be empty")
    @Size(min = 13, max = 19, message = "Card number length should be between 13 and 19")
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String card_number;
    
    /**
     * 安全码
     * 通常为3-4位数字
     */
    @NotBlank(message = "Security code cannot be empty")
    @Size(min = 3, max = 4, message = "Security code length should be 3-4 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Security code must contain only digits")
    private String security_code;
    
    /**
     * 卡片到期日期
     * 格式为：yyyy-MM-dd
     */
    @NotNull(message = "Expiry date cannot be empty")
    @Future(message = "Expiry date must be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiry_date;
    
    /**
     * 发卡国家/地区
     * 如果不提供，默认为"中国"
     */
    private String country = "CN";
} 