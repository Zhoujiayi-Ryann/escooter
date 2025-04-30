package com.example.hello.mapper;

import com.example.hello.entity.FeedbackImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 反馈图片数据访问接口
 */
@Mapper
public interface FeedbackImageMapper {
    
    /**
     * 批量插入图片
     */
    @Insert("<script>" +
            "INSERT INTO feedback_images (feedback_id, image_url, upload_time) VALUES " +
            "<foreach collection='images' item='image' separator=','>" +
            "(#{image.feedbackId}, #{image.imageUrl}, #{image.uploadTime})" +
            "</foreach>" +
            "</script>")
    void batchInsertImages(@Param("images") List<FeedbackImage> images);
    
    /**
     * 根据反馈ID获取所有图片
     */
    @Select("SELECT * FROM feedback_images WHERE feedback_id = #{feedbackId}")
    List<FeedbackImage> getFeedbackImagesById(Long feedbackId);
    
    /**
     * 根据反馈ID获取所有图片URL
     * 直接返回image_url字段值列表
     */
    @Select("SELECT image_url FROM feedback_images WHERE feedback_id = #{feedbackId}")
    List<String> getFeedbackImageUrlsById(Long feedbackId);
    
    /**
     * 统计反馈图片数量
     */
    @Select("SELECT COUNT(*) FROM feedback_images WHERE feedback_id = #{feedbackId}")
    int countImagesByFeedbackId(Long feedbackId);
    
    /**
     * 删除反馈图片
     */
    @Delete("DELETE FROM feedback_images WHERE feedback_id = #{feedbackId}")
    int deleteImagesByFeedbackId(Long feedbackId);
} 