package com.example.hello.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 验证码实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {
    
    private Integer id;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 验证码类型（如：密码重置，注册等）
     */
    private String type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
    
    /**
     * 是否已使用
     */
    private Boolean used;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
    
    /**
     * 检查验证码是否有效
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expiresAt);
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", used=" + used +
                '}';
    }
} 