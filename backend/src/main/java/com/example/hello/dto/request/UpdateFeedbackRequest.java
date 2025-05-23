package com.example.hello.dto.request;

import com.example.hello.entity.Feedback;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 更新反馈请求DTO
 */
@Data
public class UpdateFeedbackRequest {
    /**
     * 反馈描述
     */
    @NotBlank(message = "Feedback description cannot be empty")
    @Size(max = 1000, message = "Feedback description cannot exceed 1000 characters")
    private String description;
    
    /**
     * 处理状态
     */
    private String status;
    
    /**
     * 处理优先级
     */
    private String priority;
    
    /**
     * Convert the status string to an enum type
     * @return the status enum
     */
    public Feedback.Status getStatusEnum() {
        if (status == null || status.isEmpty()) {
            return null;
        }
        return Feedback.Status.valueOf(status);
    }
    
    /**
     * 将优先级字符串转换为枚举类型
     * @return 优先级枚举
     */
    public Feedback.Priority getPriorityEnum() {
        if (priority == null || priority.isEmpty()) {
            return null;
        }
        return Feedback.Priority.valueOf(priority);
    }
} 