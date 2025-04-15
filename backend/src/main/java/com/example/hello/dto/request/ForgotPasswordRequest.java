package com.example.hello.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 忘记密码请求DTO
 */
public class ForgotPasswordRequest {
    
    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordRequest{" +
                "email='" + email + '\'' +
                '}';
    }
} 