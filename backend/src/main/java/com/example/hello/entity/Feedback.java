package com.example.hello.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户反馈实体类
 */
public class Feedback {
    
    /**
     * 反馈类型枚举
     */
    public enum FeedbackType {
        /** 使用问题 */
        USING_PROBLEM,
        /** 体验反馈 */
        EXPERIENCE_FEEDBACK
    }
    
    /**
     * 处理状态枚举
     */
    public enum Status {
        /** 待处理 */
        PENDING,
        /** 处理中 */
        PROCESSING,
        /** 已解决 */
        RESOLVED,
        /** 已拒绝 */
        REJECTED
    }
    
    /**
     * 优先级枚举
     */
    public enum Priority {
        /** 低优先级 */
        LOW,
        /** 中优先级 */
        MEDIUM,
        /** 高优先级 */
        HIGH,
        /** 紧急 */
        URGENT
    }
    
    private Long id;
    private Long userId;
    private FeedbackType feedbackType;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate happeningTime;
    private String billNumber;
    private LocalDateTime createdAt;
    
    // 一对多关系：一个反馈可以有多张图片
    private List<FeedbackImage> images;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public FeedbackType getFeedbackType() {
        return feedbackType;
    }
    
    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public LocalDate getHappeningTime() {
        return happeningTime;
    }
    
    public void setHappeningTime(LocalDate happeningTime) {
        this.happeningTime = happeningTime;
    }
    
    public String getBillNumber() {
        return billNumber;
    }
    
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<FeedbackImage> getImages() {
        return images;
    }
    
    public void setImages(List<FeedbackImage> images) {
        this.images = images;
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", userId=" + userId +
                ", feedbackType=" + feedbackType +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", happeningTime=" + happeningTime +
                ", billNumber='" + billNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
} 