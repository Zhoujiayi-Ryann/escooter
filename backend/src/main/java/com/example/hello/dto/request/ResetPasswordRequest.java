package com.example.hello.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 重设密码请求DTO
 */
public class ResetPasswordRequest {
    
    /**
     * 用户邮箱
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    
    /**
     * 验证码
     */
    @NotBlank(message = "Verification code cannot be empty")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must be 6 digits")
    private String code;
    
    /**
     * 新密码
     */
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20 characters")
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", newPassword='[PROTECTED]'" +
                '}';
    }
} 