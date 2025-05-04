package com.example.hello.mapper;

import com.example.hello.entity.Feedback;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import java.util.List;

/**
 * 反馈数据访问接口
 */
@Mapper
public interface FeedbackMapper {
    
    /**
     * 插入反馈记录并返回生成的ID
     * 
     * @param feedback 反馈实体
     * @return 影响的行数
     */
    @Insert({
        "INSERT INTO feedback (user_id, feedback_type, description, status, priority, happening_time, bill_number, created_at) ",
        "VALUES (#{userId}, #{feedbackType}, #{description}, #{status}, #{priority}, #{happeningTime}, #{billNumber}, #{createdAt})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertFeedback(Feedback feedback);
    
    /**
     * 根据ID查询反馈
     * 
     * @param id 反馈ID
     * @return 反馈实体
     */
    @Select("SELECT * FROM feedback WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "feedbackType", column = "feedback_type"),
        @Result(property = "description", column = "description"),
        @Result(property = "status", column = "status"),
        @Result(property = "priority", column = "priority"),
        @Result(property = "happeningTime", column = "happening_time"),
        @Result(property = "billNumber", column = "bill_number"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "adminReply", column = "admin_reply"),
        @Result(property = "replyAdminId", column = "reply_admin_id"),
        @Result(property = "repliedAt", column = "replied_at")
    })
    Feedback findById(Long id);
    
    /**
     * 根据订单号查询反馈ID
     * 
     * @param billNumber 订单号
     * @return 反馈ID
     */
    @Select("SELECT id FROM feedback WHERE bill_number = #{billNumber} LIMIT 1")
    Long getFeedbackIdByBillNumber(String billNumber);
    
    /**
     * 查询所有反馈记录，按创建时间降序排列
     * 
     * @return 反馈记录列表
     */
    @Select("SELECT * FROM feedback ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "feedbackType", column = "feedback_type"),
        @Result(property = "description", column = "description"),
        @Result(property = "status", column = "status"),
        @Result(property = "priority", column = "priority"),
        @Result(property = "happeningTime", column = "happening_time"),
        @Result(property = "billNumber", column = "bill_number"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "adminReply", column = "admin_reply"),
        @Result(property = "replyAdminId", column = "reply_admin_id"),
        @Result(property = "repliedAt", column = "replied_at")
    })
    List<Feedback> findAllFeedback();
    
    /**
     * 更新反馈信息
     */
    @Update("UPDATE feedback SET description = #{description}, status = #{status}, priority = #{priority} WHERE id = #{id}")
    int updateFeedback(Feedback feedback);
    
    /**
     * 更新反馈回复信息
     * 
     * @param feedback 带有回复信息的反馈实体
     * @return 影响的行数
     */
    @Update({
        "UPDATE feedback SET ",
        "status = #{status}, ",
        "admin_reply = #{adminReply}, ",
        "replied_at = #{repliedAt}, ",
        "reply_admin_id = #{replyAdminId} ",
        "WHERE id = #{id}"
    })
    int updateFeedbackWithReply(Feedback feedback);
    
    /**
     * 删除反馈
     */
    @Delete("DELETE FROM feedback WHERE id = #{id}")
    int deleteFeedback(Long id);
    
    /**
     * 统计反馈关联的图片数量
     */
    @Select("SELECT COUNT(*) FROM feedback_images WHERE feedback_id = #{feedbackId}")
    int countFeedbackImages(Long feedbackId);
} 