package com.example.hello.config;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * MyBatis异常处理器
 * 用于简化MyBatis异常的输出格式
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyBatisExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisExceptionHandler.class);

    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseEntity<Map<String, String>> handleMyBatisSystemException(MyBatisSystemException e) {
        // 获取根本原因
        Throwable rootCause = getRootCause(e);
        String errorMessage = "数据库操作异常";
        
        // 检查是否是枚举转换错误
        if (rootCause instanceof IllegalArgumentException && 
            rootCause.getMessage() != null && 
            rootCause.getMessage().contains("No enum constant")) {
            
            String message = rootCause.getMessage();
            // 使用默认的日志格式记录错误
            logger.error("枚举转换错误", rootCause);
            errorMessage = "枚举值转换错误: " + message;
        } else {
            // 其他MyBatis异常，使用默认的日志格式
            logger.error("MyBatis异常", e);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * 获取异常的根本原因
     */
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return getRootCause(cause);
        }
        return throwable;
    }
} 