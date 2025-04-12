package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.response.FeedbackResponse;
import com.example.hello.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反馈控制器
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * 提交反馈
     * 
     * @param request 反馈请求
     * @return 反馈响应
     */
    @PostMapping("/submit")
    public Result<FeedbackResponse> submitFeedback(@RequestBody @Validated FeedbackRequest request) {
        logger.info("Received feedback submission request: {}", request);
        
        try {
            FeedbackResponse response = feedbackService.submitFeedback(request);
            return Result.success(response);
        } catch (Exception e) {
            logger.error("Failed to submit feedback", e);
            return Result.error(e.getMessage());
        }
    }
} 