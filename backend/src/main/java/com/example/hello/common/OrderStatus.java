package com.example.hello.common;

/**
 * 订单状态枚举类
 * 用于规范订单状态的使用
 */
public enum OrderStatus {
    /**
     * 待支付
     */
    PENDING("pending"),
    
    /**
     * 已支付
     */
    PAID("paid"),
    
    /**
     * 进行中
     */
    ACTIVE("active"),
    
    /**
     * 已完成
     */
    COMPLETED("completed"),
    
    /**
     * 已取消
     */
    CANCELLED("cancelled");
    
    private final String value;
    
    OrderStatus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 根据字符串获取对应的枚举值
     * 
     * @param value 订单状态字符串
     * @return 对应的枚举值，如果不匹配则返回null
     */
    public static OrderStatus fromValue(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
} 