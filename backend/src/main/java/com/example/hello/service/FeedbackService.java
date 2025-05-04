package com.example.hello.service;

import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.request.UpdateFeedbackRequest;
import com.example.hello.dto.request.FeedbackReplyRequest;
import com.example.hello.dto.response.DeleteFeedbackResponse;
import com.example.hello.dto.response.FeedbackDetailResponse;
import com.example.hello.dto.response.FeedbackListResponse;
import com.example.hello.dto.response.FeedbackResponse;
import com.example.hello.dto.response.FeedbackReplyResponse;

import java.util.List;

/**
 * 反馈服务接口
 */
public interface FeedbackService {
    
    /**
     * 提交反馈
     * 
     * @param request 反馈请求
     * @return 反馈响应
     */
    FeedbackResponse submitFeedback(FeedbackRequest request);
    
    /**
     * 获取所有反馈记录
     * 
     * @return 反馈列表响应
     */
    List<FeedbackListResponse> getAllFeedback();
    
    /**
     * 获取反馈详情
     * 
     * @param id 反馈ID
     * @return 反馈详情
     */
    FeedbackDetailResponse getFeedbackDetail(Long id);
    
    /**
     * 更新反馈
     * 
     * @param id 反馈ID
     * @param request 更新请求
     * @return 更新后的反馈详情
     */
    FeedbackDetailResponse updateFeedback(Long id, UpdateFeedbackRequest request);
    
    /**
     * 删除反馈
     * 
     * @param id 反馈ID
     * @return 删除结果
     */
    DeleteFeedbackResponse deleteFeedback(Long id);
    
    /**
     * 管理员回复评论
     * 
     * @param id 反馈ID
     * @param request 回复请求
     * @return 回复结果
     */
    FeedbackReplyResponse replyFeedback(Long id, FeedbackReplyRequest request);
} 