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
    @NotNull(message = "用户ID不能为空")
    private Integer user_id;
    
    /**
     * 银行卡号
     * 通常为16位数字
     */
    @NotBlank(message = "银行卡号不能为空")
    @Size(min = 13, max = 19, message = "银行卡号长度应在13-19位之间")
    @Pattern(regexp = "^[0-9]+$", message = "银行卡号只能包含数字")
    private String card_number;
    
    /**
     * 安全码
     * 通常为3-4位数字
     */
    @NotBlank(message = "安全码不能为空")
    @Size(min = 3, max = 4, message = "安全码长度应为3-4位")
    @Pattern(regexp = "^[0-9]+$", message = "安全码只能包含数字")
    private String security_code;
    
    /**
     * 卡片到期日期
     * 格式为：yyyy-MM-dd
     */
    @NotNull(message = "到期日期不能为空")
    @Future(message = "到期日期必须是将来的日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiry_date;
    
    /**
     * 发卡国家/地区
     * 如果不提供，默认为"中国"
     */
    private String country = "中国";
} 