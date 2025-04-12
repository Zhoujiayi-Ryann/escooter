package com.example.hello.entity;

import java.time.LocalDateTime;

/**
 * 反馈图片实体类
 */
public class FeedbackImage {
    private Long id;
    private Long feedbackId;
    private String imageUrl;
    private LocalDateTime uploadTime;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    @Override
    public String toString() {
        return "FeedbackImage{" +
                "id=" + id +
                ", feedbackId=" + feedbackId +
                ", imageUrl='" + imageUrl + '\'' +
                ", uploadTime=" + uploadTime +
                '}';
    }
} 