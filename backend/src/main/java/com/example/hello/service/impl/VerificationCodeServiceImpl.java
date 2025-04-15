package com.example.hello.service.impl;

import com.example.hello.entity.VerificationCode;
import com.example.hello.mapper.VerificationCodeMapper;
import com.example.hello.service.EmailService;
import com.example.hello.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 验证码服务实现类
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeMapper verificationCodeMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    // 验证码有效期（分钟）
    private static final int CODE_EXPIRATION_MINUTES = 1;
    
    // 验证码长度
    private static final int CODE_LENGTH = 6;

    @Override
    @Transactional
    public String generateAndSendCode(String email, String type) {
        log.info("开始为邮箱 {} 生成 {} 类型的验证码", email, type);
        
        // 使之前的同类型验证码失效
        invalidateAllCodes(email, type);
        
        // 生成6位随机数字验证码
        String code = generateRandomCode(CODE_LENGTH);
        
        // 创建验证码记录
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setType(type);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
        verificationCode.setUsed(false);
        
        // 保存到数据库
        verificationCodeMapper.insert(verificationCode);
        log.info("验证码 {} 已保存到数据库，类型：{}, 邮箱：{}", code, type, email);
        
        // 发送验证码邮件
        sendVerificationEmail(email, code, type);
        
        return code;
    }

    @Override
    public boolean verifyCode(String email, String code, String type) {
        log.info("验证邮箱 {} 的验证码 {}, 类型: {}", email, code, type);
        
        VerificationCode verificationCode = verificationCodeMapper.findValidCode(
                email, code, type, LocalDateTime.now());
        
        boolean valid = verificationCode != null;
        log.info("验证码验证结果: {}", valid ? "有效" : "无效");
        
        return valid;
    }

    @Override
    @Transactional
    public boolean useCode(String email, String code, String type) {
        log.info("标记验证码为已使用，邮箱: {}, 验证码: {}, 类型: {}", email, code, type);
        
        VerificationCode verificationCode = verificationCodeMapper.findValidCode(
                email, code, type, LocalDateTime.now());
        
        if (verificationCode == null) {
            log.warn("找不到有效的验证码，无法标记为已使用");
            return false;
        }
        
        int affected = verificationCodeMapper.markAsUsed(verificationCode.getId());
        boolean success = affected > 0;
        log.info("标记验证码为已使用结果: {}", success ? "成功" : "失败");
        
        return success;
    }

    @Override
    @Transactional
    public boolean invalidateAllCodes(String email, String type) {
        log.info("使邮箱 {} 的所有 {} 类型验证码失效", email, type);
        
        int affected = verificationCodeMapper.invalidateAllCodes(email, type);
        log.info("已使 {} 个验证码失效", affected);
        
        return affected >= 0; // 即使没有验证码被标记为失效，也认为操作成功
    }
    
    /**
     * 生成指定长度的随机数字验证码
     *
     * @param length 验证码长度
     * @return 随机数字验证码
     */
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * 发送验证码邮件
     *
     * @param email 收件人邮箱
     * @param code  验证码
     * @param type  验证码类型
     */
    private void sendVerificationEmail(String email, String code, String type) {
        try {
            Context context = new Context();
            context.setVariable("verificationCode", code);
            
            String subject;
            String templateName;
            
            if ("RESET_PASSWORD".equals(type)) {
                subject = "密码重置验证码";
                templateName = "mail/reset-password";
            } else if ("REGISTER".equals(type)) {
                subject = "注册验证码";
                templateName = "mail/register-verification";
            } else {
                subject = "验证码";
                templateName = "mail/general-verification";
            }
            
            String content = templateEngine.process(templateName, context);
            emailService.sendHtmlEmail(email, subject, content);
            
            log.info("验证码邮件发送成功，邮箱: {}, 类型: {}", email, type);
        } catch (Exception e) {
            log.error("发送验证码邮件失败: {}", e.getMessage(), e);
        }
    }
} 