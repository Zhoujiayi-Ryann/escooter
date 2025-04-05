package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.AddCreditCardRequest;
import com.example.hello.dto.AddCreditCardResponse;
import com.example.hello.dto.CreditCardResponse;
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
            return ResponseEntity.badRequest().body(Result.error("用户ID不能为空"));
        }
        if (request.getCard_number() == null || request.getCard_number().isEmpty()) {
            return ResponseEntity.badRequest().body(Result.error("银行卡号不能为空"));
        }
        if (request.getSecurity_code() == null || request.getSecurity_code().isEmpty()) {
            return ResponseEntity.badRequest().body(Result.error("安全码不能为空"));
        }
        if (request.getExpiry_date() == null) {
            return ResponseEntity.badRequest().body(Result.error("到期日期不能为空"));
        }
        if (request.getExpiry_date().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Result.error("到期日期必须是将来的日期"));
        }

        // 控制器层额外验证银行卡号
        if (!CreditCardUtils.isValidCardNumber(request.getCard_number())) {
            return ResponseEntity.badRequest().body(Result.error("银行卡号无效"));
        }
        
        // 调用服务添加银行卡
        Optional<AddCreditCardResponse> responseOpt = creditCardService.addCreditCard(request);
        
        if (responseOpt.isPresent()) {
            return ResponseEntity.ok(Result.success(responseOpt.get(), "银行卡添加成功"));
        } else {
            return ResponseEntity.badRequest().body(Result.error("添加银行卡失败"));
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
        log.info("获取用户银行卡列表: userId={}", userId);
        
        // 参数校验
        if (userId == null) {
            return ResponseEntity.badRequest().body(Result.error("用户ID不能为空"));
        }
        
        // 调用服务获取银行卡列表
        List<CreditCardResponse> cards = creditCardService.getCreditCardsByUserId(userId);
        return ResponseEntity.ok(Result.success(cards, "获取银行卡列表成功"));
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
        log.info("删除银行卡: cardId={}, userId={}", cardId, userId);
        
        // 参数校验
        if (cardId == null) {
            return ResponseEntity.badRequest().body(Result.error("银行卡ID不能为空"));
        }
        if (userId == null) {
            return ResponseEntity.badRequest().body(Result.error("用户ID不能为空"));
        }
        
        // 校验银行卡是否属于该用户
        if (!creditCardService.checkCardBelongsToUser(cardId, userId)) {
            return ResponseEntity.badRequest().body(Result.error("此银行卡不存在于此用户"));
        }
        
        // 调用服务删除银行卡
        boolean success = creditCardService.deleteCreditCard(cardId);
        if (success) {
            return ResponseEntity.ok(Result.success("删除成功", "删除银行卡成功"));
        } else {
            return ResponseEntity.badRequest().body(Result.error("删除银行卡失败"));
        }
    }
} 