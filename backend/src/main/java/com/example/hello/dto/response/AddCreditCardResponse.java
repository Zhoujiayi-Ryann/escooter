package com.example.hello.dto.response;

import lombok.Data;

/**
 * 添加银行卡响应DTO
 */
@Data
public class AddCreditCardResponse {
    /**
     * 银行卡ID
     */
    private Integer card_id;
    
    /**
     * 静态工厂方法，创建成功响应
     * 
     * @param cardId 银行卡ID
     * @return 响应对象
     */
    public static AddCreditCardResponse success(Integer cardId) {
        AddCreditCardResponse response = new AddCreditCardResponse();
        response.setCard_id(cardId);
        return response;
    }
} 