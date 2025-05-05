package com.example.hello.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求DTO
 */
@Data
public class ChangePasswordRequest {
    /**
     * 原密码
     */
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    /**
     * 新密码，长度至少6个字符
     */
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
}