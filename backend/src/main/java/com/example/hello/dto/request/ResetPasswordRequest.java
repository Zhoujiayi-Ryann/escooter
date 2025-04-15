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
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    private String code;
    
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min =
6, max = 20, message = "密码长度必须在6-20个字符之间")
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