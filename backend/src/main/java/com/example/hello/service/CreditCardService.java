package com.example.hello.service;

import com.example.hello.dto.request.AddCreditCardRequest;
import com.example.hello.dto.response.AddCreditCardResponse;
import com.example.hello.dto.response.CreditCardResponse;

import java.util.List;
import java.util.Optional;

/**
 * 银行卡服务接口
 */
public interface CreditCardService {
    
    /**
     * 添加银行卡
     * 
     * @param request 添加银行卡请求
     * @return 添加结果，包含银行卡ID
     */
    Optional<AddCreditCardResponse> addCreditCard(AddCreditCardRequest request);
    
    /**
     * 查询用户银行卡列表
     * 
     * @param userId 用户ID
     * @return 银行卡列表
     */
    List<CreditCardResponse> getCreditCardsByUserId(Integer userId);
    
    /**
     * 删除银行卡
     * 
     * @param cardId 银行卡ID
     * @return 是否删除成功
     */
    boolean deleteCreditCard(Integer cardId);
    
    /**
     * 检查银行卡是否属于指定用户
     * 
     * @param cardId 银行卡ID
     * @param userId 用户ID
     * @return 是否属于该用户
     */
    boolean checkCardBelongsToUser(Integer cardId, Integer userId);
} 