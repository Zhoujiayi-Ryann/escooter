package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 反馈提交响应DTO
 */
public class FeedbackResponse {
    @JsonProperty("feedback_id")
    private Long feedbackId;
    
    @JsonProperty("order_id")
    private Long orderId;
    
    @JsonProperty("image_urls")
    private List<String> imageUrls;
    
    // 静态工厂方法，便于创建实例
    public static FeedbackResponse of(Long feedbackId, Long orderId, List<String> imageUrls) {
        FeedbackResponse response = new FeedbackResponse();
        response.setFeedbackId(feedbackId);
        response.setOrderId(orderId);
        response.setImageUrls(imageUrls);
        return response;
    }
    
    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "feedbackId=" + feedbackId +
                ", orderId=" + orderId +
                ", imageUrls=" + imageUrls +
                '}';
    }
} 