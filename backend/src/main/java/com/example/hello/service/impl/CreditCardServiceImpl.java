package com.example.hello.service.impl;

import com.example.hello.dto.request.AddCreditCardRequest;
import com.example.hello.dto.response.AddCreditCardResponse;
import com.example.hello.dto.response.CreditCardResponse;
import com.example.hello.entity.CreditCard;
import com.example.hello.entity.User;
import com.example.hello.mapper.CreditCardMapper;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.CreditCardService;
import com.example.hello.utils.CreditCardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 银行卡服务实现类
 */
@Service
@Slf4j
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardMapper creditCardMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    @Transactional
    public Optional<AddCreditCardResponse> addCreditCard(AddCreditCardRequest request) {
        log.info(" Add Credit Card: userId={}", request.getUser_id());
        
        try {
            // 1. 验证用户是否存在
            User user = userMapper.findById(request.getUser_id().longValue());
            if (user == null) {
                log.warn("Add Credit Card Failed: UserId{} does not exist", request.getUser_id());
                return Optional.empty();
            }
            
            // 2. 验证银行卡号是否有效
            if (!CreditCardUtils.isValidCardNumber(request.getCard_number())) {
                log.warn("Add Credit Card Failed: Invalid card number");
                return Optional.empty();
            }
            
            // 3. 检查银行卡号是否已存在
            int count = creditCardMapper.countByCardNumber(request.getCard_number());
            if (count > 0) {
                log.warn("Add Credit Card Failed: Card number already exists");
                return Optional.empty();
            }
            
            // 4. 创建银行卡对象并保存
            CreditCard creditCard = new CreditCard();
            creditCard.setUserId(request.getUser_id());
            creditCard.setCardNumber(request.getCard_number());
            creditCard.setSecurityCode(request.getSecurity_code());
            creditCard.setExpiryDate(request.getExpiry_date());
            creditCard.setCountry(request.getCountry());
            
            // 5. 插入数据库
            int rows = creditCardMapper.addCreditCard(creditCard);
            if (rows > 0) {
                log.info("Add Credit Card Success: cardId={}, maskedCardNumber={}", 
                        creditCard.getCardId(), 
                        CreditCardUtils.maskCardNumber(creditCard.getCardNumber()));
                return Optional.of(AddCreditCardResponse.success(creditCard.getCardId()));
            } else {
                log.error("Add Credit Card Failed: Database insertion failed");
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Add Credit Card Failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<CreditCardResponse> getCreditCardsByUserId(Integer userId) {
        log.info("Get Credit Cards By UserId: userId={}", userId);
        
        try {
            // 获取该用户的所有银行卡
            List<CreditCard> cards = creditCardMapper.findByUserId(userId);
            
            // 转换为响应DTO并对卡号进行掩码处理
            return cards.stream()
                    .map(card -> {
                        CreditCardResponse response = new CreditCardResponse();
                        response.setCard_id(card.getCardId());
                        // 对卡号进行掩码处理
                        response.setCard_number(CreditCardUtils.maskCardNumber(card.getCardNumber()));
                        response.setExpiry_date(card.getExpiryDate());
                        response.setCountry(card.getCountry());
                        return response;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Get Credit Cards By UserId Failed: userId={}", userId, e);
            return List.of(); // 返回空列表
        }
    }
    
    @Override
    @Transactional
    public boolean deleteCreditCard(Integer cardId) {
        log.info("Delete Credit Card: cardId={}", cardId);
        
        try {
            // 1. 验证银行卡是否存在
            CreditCard card = creditCardMapper.findById(cardId);
            if (card == null) {
                log.warn("Delete Credit Card Failed: CardId{} does not exist", cardId);
                return false;
            }
            
            // 2. 执行删除操作
            int rows = creditCardMapper.deleteById(cardId);
            if (rows > 0) {
                log.info("Delete Credit Card Success: cardId={}", cardId);
                return true;
            } else {
                log.error("Delete Credit Card Failed: Database operation did not affect any rows");
                return false;
            }
        } catch (Exception e) {
            log.error("Delete Credit Card Failed: cardId={}", cardId, e);
            return false;
        }
    }
    
    @Override
    public boolean checkCardBelongsToUser(Integer cardId, Integer userId) {
        try {
            int count = creditCardMapper.checkCardBelongsToUser(cardId, userId);
            return count > 0;
        } catch (Exception e) {
            log.error("Check Card Belongs To User Failed: cardId={}, userId={}", cardId, userId, e);
            return false;
        }
    }
} 