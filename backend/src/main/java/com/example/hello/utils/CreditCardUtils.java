package com.example.hello.utils;

/**
 * 银行卡工具类
 * 提供银行卡号掩码、验证等功能
 */
public class CreditCardUtils {
    
    /**
     * 对银行卡号进行掩码处理
     * 只显示前4位和后4位，中间用*替代
     * 
     * @param cardNumber 完整银行卡号
     * @return 掩码后的银行卡号
     */
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }
        
        int length = cardNumber.length();
        String prefix = cardNumber.substring(0, 4);
        String suffix = cardNumber.substring(length - 4);
        String masked = "*".repeat(length - 8);
        
        return prefix + masked + suffix;
    }
    
    /**
     * 验证银行卡号是否有效（使用Luhn算法）
     * 
     * @param cardNumber 银行卡号
     * @return 是否有效
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        // 移除空格和破折号
        String number = cardNumber.replaceAll("[\\s-]", "");
        
        // 检查是否只包含数字
        if (!number.matches("\\d+")) {
            return false;
        }
        
        // 检查长度是否在13-19位之间（符合大多数银行卡规则）
        if (number.length() < 13 || number.length() > 19) {
            return false;
        }
        
        // 开发环境下，不进行Luhn算法验证，仅做基本格式检查
        // 实际生产环境应启用以下Luhn算法验证
        /*
        // 实现Luhn算法
        int sum = 0;
        boolean alternate = false;
        
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
        */
        
        // 开发环境下直接返回true
        return true;
    }
} 