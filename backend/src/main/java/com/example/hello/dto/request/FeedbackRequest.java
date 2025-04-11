package com.example.hello.dto.request;

import com.example.hello.entity.Feedback;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 反馈提交请求DTO
 */
public class FeedbackRequest {
    @NotNull(message = "用户ID不能为空")
    @JsonProperty("user_id")
    private Long userId;
    
    @NotNull(message = "反馈类型不能为空")
    @JsonProperty("feedback_type")
    private Feedback.FeedbackType feedbackType;
    
    @NotBlank(message = "反馈描述不能为空")
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("happening_time")
    private LocalDate happeningTime;
    
    @JsonProperty("bill_number")
    private String billNumber;
    
    @JsonProperty("image_urls")
    private List<String> imageUrls;
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Feedback.FeedbackType getFeedbackType() {
        return feedbackType;
    }
    
    public void setFeedbackType(Feedback.FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "userId=" + userId +
                ", feedbackType=" + feedbackType +
                ", description='" + description + '\'' +
                ", happeningTime=" + happeningTime +
                ", billNumber='" + billNumber + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }
} 