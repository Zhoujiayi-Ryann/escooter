package com.example.hello.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户信息请求DTO
 */
@Data
public class UpdateUserRequest {
    /**
     * 用户名，长度3-15个字符
     */
    @Size(min = 3, max = 15, message = "用户名长度必须在3-15个字符之间")
    private String username;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户手机号，格式为1开头的11位数字
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone_number;

    private String avatar_path;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }
}