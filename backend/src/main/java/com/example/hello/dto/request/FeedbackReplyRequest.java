package com.example.hello.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 反馈回复请求DTO
 */
@Data
public class FeedbackReplyRequest {

    /**
     * 管理员ID
     */
    @JsonProperty("admin_id")
    private Long adminId;

    /**
     * 回复内容
     */
    @JsonProperty("content")
    private String content;

    /**
     * 是否发送通知
     */
    @JsonProperty("send_notification")
    private boolean sendNotification = true;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return "FeedbackReplyRequest{" +
                "adminId=" + adminId +
                ", content='" + content + '\'' +
                ", sendNotification=" + sendNotification +
                '}';
    }
} 