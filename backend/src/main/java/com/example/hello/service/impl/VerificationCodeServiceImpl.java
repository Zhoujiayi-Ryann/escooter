package com.example.hello.service.impl;

import com.example.hello.service.EmailService;
import com.example.hello.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Verification code service implementation - Memory storage version
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    /**
     * In-memory data structure for storing verification codes
     * Format: "email:type" -> [code, expirationTime, isUsed]
     */
    private static final Map<String, Object[]> CODE_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    // Verification code validity period (minutes)
    private static final int CODE_EXPIRATION_MINUTES = 5;
    
    // Verification code length
    private static final int CODE_LENGTH = 6;

    @Override
    public String generateAndSendCode(String email, String type) {
        log.info("Generating verification code for email: {}, type: {}", email, type);
        
        // Invalidate previous codes of the same type
        invalidateAllCodes(email, type);
        
        // Generate a 6-digit random numeric verification code
        String code = generateRandomCode(CODE_LENGTH);
        
        // Calculate expiration time
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);
        
        // Save to memory cache
        String key = generateKey(email, type);
        CODE_CACHE.put(key, new Object[]{code, expiresAt, false});
        
        log.info("Verification code generated: {}, for email: {}, type: {}", code, email, type);
        
        // Send verification email
        sendVerificationEmail(email, code, type);
        
        return code;
    }

    @Override
    public boolean verifyCode(String email, String code, String type) {
        log.info("Verifying code for email: {}, type: {}", email, type);
        
        String key = generateKey(email, type);
        Object[] codeData = CODE_CACHE.get(key);
        
        if (codeData == null) {
            log.warn("No verification code record found for email: {}, type: {}", email, type);
            return false;
        }
        
        String savedCode = (String) codeData[0];
        LocalDateTime expiresAt = (LocalDateTime) codeData[1];
        boolean isUsed = (boolean) codeData[2];
        
        LocalDateTime now = LocalDateTime.now();
        
        boolean valid = savedCode.equals(code) && !isUsed && now.isBefore(expiresAt);
        log.info("Verification result: {}, for email: {}, type: {}", valid ? "valid" : "invalid", email, type);
        
        return valid;
    }

    @Override
    public boolean useCode(String email, String code, String type) {
        log.info("Marking code as used for email: {}, type: {}", email, type);
        
        String key = generateKey(email, type);
        Object[] codeData = CODE_CACHE.get(key);
        
        if (codeData == null) {
            log.warn("No verification code record found for email: {}, type: {}", email, type);
            return false;
        }
        
        String savedCode = (String) codeData[0];
        LocalDateTime expiresAt = (LocalDateTime) codeData[1];
        
        LocalDateTime now = LocalDateTime.now();
        
        if (!savedCode.equals(code) || now.isAfter(expiresAt)) {
            log.warn("Code is invalid or expired for email: {}, type: {}", email, type);
            return false;
        }
        
        // Mark as used
        codeData[2] = true;
        CODE_CACHE.put(key, codeData);
        
        log.info("Code marked as used successfully for email: {}, type: {}", email, type);
        return true;
    }

    @Override
    public boolean invalidateAllCodes(String email, String type) {
        log.info("Invalidating all codes for email: {}, type: {}", email, type);
        
        String key = generateKey(email, type);
        CODE_CACHE.remove(key);
        
        log.info("All codes invalidated for email: {}, type: {}", email, type);
        return true;
    }
    
    /**
     * Generate cache key
     */
    private String generateKey(String email, String type) {
        return email + ":" + type;
    }
    
    /**
     * Generate a random numeric verification code of specified length
     *
     * @param length Verification code length
     * @return Random numeric verification code
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
     * Send verification code email
     *
     * @param email Recipient email
     * @param code  Verification code
     * @param type  Verification code type
     */
    private void sendVerificationEmail(String email, String code, String type) {
        try {
            Context context = new Context();
            context.setVariable("verificationCode", code);
            
            String subject;
            String templateName;
            
            if ("RESET_PASSWORD".equals(type)) {
                subject = "Password Reset Verification Code";
                templateName = "mail/reset-password";
                
                // For development: Log the code
                log.info("Password reset verification code: {} has been generated for: {}", code, email);
            } else if ("REGISTER".equals(type)) {
                subject = "Registration Verification Code";
                templateName = "mail/register-verification";
            } else {
                subject = "Verification Code";
                templateName = "mail/general-verification";
            }
            
            // Enable email sending
            String content = templateEngine.process(templateName, context);
            emailService.sendHtmlEmail(email, subject, content);
            
            log.info("Verification email sent successfully to: {}, type: {}", email, type);
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}, type: {}, error: {}", email, type, e.getMessage(), e);
        }
    }
} 