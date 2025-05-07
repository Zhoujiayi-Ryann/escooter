package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.AddCreditCardRequest;
import com.example.hello.dto.response.AddCreditCardResponse;
import com.example.hello.dto.response.CreditCardResponse;
import com.example.hello.service.CreditCardService;
import com.example.hello.utils.CreditCardUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 银行卡控制器
 * 提供银行卡相关的API接口
 */
@RestController
@RequestMapping("/api/bank-cards")
@Slf4j
public class BankCardController {

    @Autowired
    private CreditCardService creditCardService;
    
    /**
     * 添加银行卡接口
     * 
     * @param request 添加银行卡请求
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<Result<AddCreditCardResponse>> addBankCard(@RequestBody AddCreditCardRequest request) {
        // 参数校验
        if (request.getUser_id() == null) {
            return ResponseEntity.badRequest().body(Result.error("User ID is required"));
        }
        if (request.getCard_number() == null || request.getCard_number().isEmpty()) {
            return ResponseEntity.badRequest().body(Result.error("Card number is required"));
        }
        if (request.getSecurity_code() == null || request.getSecurity_code().isEmpty()) {
            return ResponseEntity.badRequest().body(Result.error("Security code is requiredy code is requiredy code is required"));
        }
        if (request.getExpiry_date() == null) {
            return ResponseEntity.badRequest().body(Result.error("Expiry date is required"));
        }
        if (request.getExpiry_date().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Result.error("Expiry date must be a future date"));
        }

        // 控制器层额外验证银行卡号
        if (!CreditCardUtils.isValidCardNumber(request.getCard_number())) {
            return ResponseEntity.badRequest().body(Result.error("Invalid card number"));
        }
        
        // 调用服务添加银行卡
        Optional<AddCreditCardResponse> responseOpt = creditCardService.addCreditCard(request);
        
        if (responseOpt.isPresent()) {
            return ResponseEntity.ok(Result.success(responseOpt.get(), "Successfully added bank cardSuccessfully added bank card"));
        } else {
            return ResponseEntity.badRequest().body(Result.error("Failed to add bank card"));
        }
    }
    
    /**
     * 获取用户的银行卡列表
     * 
     * @param userId 用户ID
     * @return 银行卡列表
     */
    @GetMapping
    public ResponseEntity<Result<List<CreditCardResponse>>> getBankCards(@RequestParam Integer userId) {
        log.info("Get Credit Cards By UserId: userId={}", userId);
        
        // 参数校验
        if (userId == null) {
            return ResponseEntity.badRequest().body(Result.error("User ID is required"));
        }
        
        // 调用服务获取银行卡列表
        List<CreditCardResponse> cards = creditCardService.getCreditCardsByUserId(userId);
        return ResponseEntity.ok(Result.success(cards, "Successfully retrieved bank card list"));
    }
    
    /**
     * 删除银行卡
     * 
     * @param cardId 银行卡ID
     * @param userId 用户ID (用于权限验证)
     * @return 删除结果
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Result<String>> deleteBankCard(@PathVariable Integer cardId, @RequestParam Integer userId) {
        log.info("Delete Credit Card: cardId={}, userId={}", cardId, userId);
        
        // 参数校验
        if (cardId == null) {
            return ResponseEntity.badRequest().body(Result.error("Card ID is required"));
        }
        if (userId == null) {
            return ResponseEntity.badRequest().body(Result.error("User ID is required"));
        }
        
        // 校验银行卡是否属于该用户
        if (!creditCardService.checkCardBelongsToUser(cardId, userId)) {
            return ResponseEntity.badRequest().body(Result.error("This card does not exist in this user"));
        }
        
        // 调用服务删除银行卡
        boolean success = creditCardService.deleteCreditCard(cardId);
        if (success) {
            return ResponseEntity.ok(Result.success("Successfully deleted", "Successfully deleted bank card"));
        } else {
            return ResponseEntity.badRequest().body(Result.error("Failed to delete bank card"));
        }
    }
} 