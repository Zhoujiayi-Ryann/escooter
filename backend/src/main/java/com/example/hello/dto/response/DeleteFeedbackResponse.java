package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 删除反馈响应DTO
 */
@Data
@Builder
public class DeleteFeedbackResponse {
    /**
     * 已删除的反馈ID
     */
    @JsonProperty("deleted_feedback_id")
    private Long deletedFeedbackId;
    
    /**
     * 已删除的图片数量
     */
    @JsonProperty("deleted_images_count")
    private int deletedImagesCount;
    
    /**
     * 静态工厂方法，创建响应对象
     * @param feedbackId 反馈ID
     * @param imagesCount 图片数量
     * @return 响应对象
     */
    public static DeleteFeedbackResponse of(Long feedbackId, int imagesCount) {
        return DeleteFeedbackResponse.builder()
                .deletedFeedbackId(feedbackId)
                .deletedImagesCount(imagesCount)
                .build();
    }
} 