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
     * @return 操作结果
     */
    @PostMapping
    public Result<FeedbackResponse> submitFeedback(@RequestBody @Validated FeedbackRequest request) {
        logger.info("接收到反馈提交请求: {}", request);
        
        try {
            FeedbackResponse response = feedbackService.submitFeedback(request);
            return Result.success(response, "反馈提交成功");
        } catch (Exception e) {
            logger.error("提交反馈失败", e);
            return Result.error("提交反馈失败: " + e.getMessage());
        }
    }
} 