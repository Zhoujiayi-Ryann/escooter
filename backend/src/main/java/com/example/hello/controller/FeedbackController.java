package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.FeedbackRequest;
import com.example.hello.dto.request.UpdateFeedbackRequest;
import com.example.hello.dto.request.FeedbackReplyRequest;
import com.example.hello.dto.response.DeleteFeedbackResponse;
import com.example.hello.dto.response.FeedbackDetailResponse;
import com.example.hello.dto.response.FeedbackListResponse;
import com.example.hello.dto.response.FeedbackResponse;
import com.example.hello.dto.response.FeedbackReplyResponse;
import com.example.hello.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 反馈控制器
 */
@RestController
@RequestMapping("/api")
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
    @PostMapping("/feedback")
    public Result<FeedbackResponse> submitFeedback(@RequestBody @Validated FeedbackRequest request) {
        logger.info("Received submit feedback request: {}", request);
        
        try {
            FeedbackResponse response = feedbackService.submitFeedback(request);
            return Result.success(response);
        } catch (Exception e) {
            logger.error("Submit feedback failed", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取所有反馈
     * 
     * @return 反馈列表
     */
    @GetMapping("/feedback_list")
    public Result<List<FeedbackListResponse>> getAllFeedback() {
        logger.info("Received get all feedback request");
        
        try {
            List<FeedbackListResponse> feedbackList = feedbackService.getAllFeedback();
            return Result.success(feedbackList);
        } catch (Exception e) {
            logger.error("Get all feedback list failed", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取反馈详情
     * 
     * @param id 反馈ID
     * @return 反馈详情
     */
    @GetMapping("/feedback/{id}")
    public Result<FeedbackDetailResponse> getFeedbackDetail(@PathVariable Long id) {
        logger.info("Received get feedback detail request, id: {}", id);
        
        try {
            FeedbackDetailResponse feedback = feedbackService.getFeedbackDetail(id);
            
            if (feedback == null) {
                logger.warn("Feedback not found, id: {}", id);
                return Result.error("Feedback not found");
            }
            
            logger.info("Successfully get feedback detail, id: {}, images count: {}", id, 
                    feedback.getImages() != null ? feedback.getImages().size() : 0);
            
            return Result.success(feedback);
        } catch (Exception e) {
            logger.error("Get feedback detail failed, id: {}, error: {}", id, e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新反馈
     * 
     * @param id 反馈ID
     * @param request 更新请求
     * @return 更新后的反馈详情
     */
    @PutMapping("/feedback/{id}")
    public Result<FeedbackDetailResponse> updateFeedback(
            @PathVariable Long id, 
            @RequestBody @Validated UpdateFeedbackRequest request) {
        logger.info("Received update feedback request, id: {}, request: {}", id, request);
        
        try {
            FeedbackDetailResponse response = feedbackService.updateFeedback(id, request);
            
            if (response == null) {
                logger.warn("Update failed, feedback not found or system error, id: {}", id);
                return Result.error("Update failed, feedback not found or system error");
            }
            
            return Result.success(response, "Feedback updated successfully");
        } catch (Exception e) {
            logger.error("Update feedback failed", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除反馈
     * 
     * @param id 反馈ID
     * @return 删除结果
     */
    @DeleteMapping("/feedback/{id}")
    public Result<DeleteFeedbackResponse> deleteFeedback(@PathVariable Long id) {
        logger.info("Received delete feedback request, id: {}", id);
        
        try {
            DeleteFeedbackResponse response = feedbackService.deleteFeedback(id);
            
            if (response == null) {
                return Result.error("Delete failed, feedback not found or system error");
            }
            
            return Result.success(response, "Feedback deleted successfully");
        } catch (Exception e) {
            logger.error("Delete feedback failed", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 管理员回复评论
     * 
     * @param id 反馈ID
     * @param request 回复请求
     * @return 回复结果
     */
    @PostMapping("/feedback/{id}/reply")
    public Result<FeedbackReplyResponse> replyFeedback(
            @PathVariable Long id,
            @RequestBody @Validated FeedbackReplyRequest request) {
        logger.info("Received admin reply to feedback, id: {}, admin id: {}", id, request.getAdminId());
        
        try {
            FeedbackReplyResponse response = feedbackService.replyFeedback(id, request);
            
            if (response == null) {
                logger.warn("Reply failed, feedback not found or system error, id: {}", id);
                return Result.error("Reply failed, feedback not found or system error");
            }
            
            logger.info("Successfully replied to feedback, id: {}, admin id: {}", id, request.getAdminId());
            return Result.success(response, "Reply submitted successfully");
        } catch (Exception e) {
            logger.error("Reply to feedback failed, id: {}, admin id: {}, error: {}", 
                    id, request.getAdminId(), e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
} 