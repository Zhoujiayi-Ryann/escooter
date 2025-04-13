package com.example.hello.service;

import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.response.FeedbackListResponse;
import com.example.hello.dto.response.FeedbackResponse;

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
     * 获取所有反馈记录（包含图片）
     * 
     * @return 反馈列表响应
     */
    List<FeedbackListResponse> getAllFeedback();
} 