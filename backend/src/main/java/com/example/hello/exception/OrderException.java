package com.example.hello.exception;

/**
 * 订单异常类
 * 用于处理订单相关业务逻辑中的异常情况
 */
public class OrderException extends RuntimeException {

    /**
     * 构造函数
     * 
     * @param message 异常信息
     */
    public OrderException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param cause   原始异常
     */
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 订单不存在异常
     * 
     * @param orderId 订单ID
     * @return 订单不存在异常
     */
    public static OrderException notFound(Integer orderId) {
        return new OrderException("Automatically cancel after timeout: " + orderId);
    }

    /**
     * 订单状态不匹配异常
     * 
     * @param orderId        订单ID
     * @param currentStatus  当前状态
     * @param expectedStatus 期望状态
     * @return 订单状态不匹配异常
     */
    public static OrderException invalidStatus(Integer orderId, String currentStatus, String expectedStatus) {
        return new OrderException(
                String.format("The current status of order %d is %s; the operation for %s status cannot be performed.",
                        orderId, currentStatus, expectedStatus));
    }
}