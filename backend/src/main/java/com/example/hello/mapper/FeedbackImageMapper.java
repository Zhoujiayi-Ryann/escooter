package com.example.hello.mapper;

import com.example.hello.entity.FeedbackImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 反馈图片Mapper接口
 */
@Mapper
public interface FeedbackImageMapper {
    
    /**
     * 批量插入反馈图片
     * 
     * @param images 反馈图片列表
     * @return 影响的行数
     */
    @Insert({
        "<script>",
        "INSERT INTO feedback_images (feedback_id, image_url, upload_time) VALUES ",
        "<foreach collection='list' item='image' separator=','>",
        "(#{image.feedbackId}, #{image.imageUrl}, #{image.uploadTime})",
        "</foreach>",
        "</script>"
    })
    int batchInsertImages(List<FeedbackImage> images);
    
    /**
     * 根据反馈ID查询图片URL列表
     * 
     * @param feedbackId 反馈ID
     * @return 图片URL列表
     */
    @Select("SELECT image_url FROM feedback_images WHERE feedback_id = #{feedbackId}")
    List<String> getImageUrlsByFeedbackId(Long feedbackId);
    
    /**
     * 根据反馈ID查询完整的图片信息
     * 
     * @param feedbackId 反馈ID
     * @return 图片实体列表
     */
    @Select("SELECT * FROM feedback_images WHERE feedback_id = #{feedbackId} ORDER BY upload_time ASC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "feedbackId", column = "feedback_id"),
        @Result(property = "imageUrl", column = "image_url"),
        @Result(property = "uploadTime", column = "upload_time")
    })
    List<FeedbackImage> getFeedbackImagesById(Long feedbackId);
} 