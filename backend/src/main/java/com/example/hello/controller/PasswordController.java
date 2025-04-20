package com.example.hello.controller;

import com.example.hello.dto.request.ForgotPasswordRequest;
import com.example.hello.dto.request.ResetPasswordRequest;
import com.example.hello.dto.request.VerifyCodeRequest;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.VerificationCodeService;
import com.example.hello.entity.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Password related controller
 * Handles forgot password, verification code validation and password reset functionality
 */
@RestController
@RequestMapping("/api/password")
@Slf4j
@Validated
public class PasswordController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private UserMapper userMapper;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String RESET_PASSWORD_CODE_TYPE = "RESET_PASSWORD";

    /**
     * Handle forgot password request, send password reset verification code to user's email
     *
     * @param request Forgot password request object
     * @return Response entity
     */
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Received forgot password request, email: {}", request.getEmail());

        // Verify if email exists
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            log.warn("Email not found: {}", request.getEmail());
            return ResponseEntity.badRequest().body("{\"code\":0,\"msg\":\"Email not found\",\"data\":null}");
        }

        try {
            // Generate verification code and send email
            String code = verificationCodeService.generateAndSendCode(
                    request.getEmail(), RESET_PASSWORD_CODE_TYPE);
            log.info("Sent reset password verification code to email: {}", request.getEmail());
            
            return ResponseEntity.ok("{\"code\":1,\"msg\":\"Reset password verification code sent to email\",\"data\":null}");
        } catch (Exception e) {
            log.error("Send reset password verification code failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"code\":0,\"msg\":\"Send verification code failed, please try again later\",\"data\":null}");
        }
    }

    /**
     * Verify if reset password verification code is valid
     *
     * @param request Verification code verification request
     * @return Response entity
     */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        log.info("Verify reset password verification code, email: {}", request.getEmail());

        boolean valid = verificationCodeService.verifyCode(
                request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);

        if (valid) {
            log.info("Verification code verified successfully, email: {}", request.getEmail());
            return ResponseEntity.ok("{\"code\":1,\"msg\":\"Verification code is valid\",\"data\":null}");
        } else {
            log.warn("Verification code verification failed, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body("{\"code\":0,\"msg\":\"Verification code is invalid or expired\",\"data\":null}");
        }
    }

    /**
     * Reset user password
     *
     * @param request Reset password request
     * @return Response entity
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Received reset password request, email: {}", request.getEmail());

        // Verification code check
        boolean valid = verificationCodeService.verifyCode(
                request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);

        if (!valid) {
            log.warn("Reset password failed: verification code is invalid, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body("{\"code\":0,\"msg\":\"Verification code is invalid or expired\",\"data\":null}");
        }

        // Find user
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            log.warn("Reset password failed: user not found, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body("{\"code\":0,\"msg\":\"User not found\",\"data\":null}");
        }

        try {
            // Update password with encryption
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userMapper.updateUser(user);
            
            // Mark verification code as used
            verificationCodeService.useCode(request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);
            
            log.info("Password reset successfully, email: {}", request.getEmail());
            return ResponseEntity.ok("{\"code\":1,\"msg\":\"Password reset successfully\",\"data\":null}");
        } catch (Exception e) {
            log.error("Reset password failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"code\":0,\"msg\":\"Reset password failed, please try again later\",\"data\":null}");
        }
    }
} 