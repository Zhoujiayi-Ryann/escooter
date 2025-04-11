package com.example.hello.service;

import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.response.FeedbackResponse;

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
} 