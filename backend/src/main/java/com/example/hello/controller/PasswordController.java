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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码相关控制器
 * 处理忘记密码、验证码验证和重置密码功能
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

    private static final String RESET_PASSWORD_CODE_TYPE = "RESET_PASSWORD";

    /**
     * 处理忘记密码请求，发送重置密码验证码到用户邮箱
     *
     * @param request 忘记密码请求对象
     * @return 响应实体
     */
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("received forgot password request, email: {}", request.getEmail());

        // 验证邮箱是否存在
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            log.warn("email not found: {}", request.getEmail());
            return ResponseEntity.badRequest().body(Result.error("email not found"));
        }

        try {
            // 生成验证码并发送邮件
            String code = verificationCodeService.generateAndSendCode(
                    request.getEmail(), RESET_PASSWORD_CODE_TYPE);
            log.info("sent reset password verification code to email: {}", request.getEmail());
            
            return ResponseEntity.ok(Result.success("reset password verification code sent to email"));
        } catch (Exception e) {
            log.error("send reset password verification code failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("send verification code failed, please try again later"));
        }
    }

    /**
     * 验证重置密码验证码是否有效
     *
     * @param request 验证码验证请求
     * @return 响应实体
     */
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        log.info("verify reset password verification code, email: {}", request.getEmail());

        boolean valid = verificationCodeService.verifyCode(
                request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);

        if (valid) {
            log.info("verification code verified successfully, email: {}", request.getEmail());
            return ResponseEntity.ok(Result.success("verification code is valid"));
        } else {
            log.warn("verification code verification failed, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body(Result.error("verification code is invalid or expired"));
        }
    }

    /**
     * 重置用户密码
     *
     * @param request 重置密码请求
     * @return 响应实体
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("received reset password request, email: {}", request.getEmail());

        // 验证码检查
        boolean valid = verificationCodeService.verifyCode(
                request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);

        if (!valid) {
            log.warn("reset password failed: verification code is invalid, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body(Result.error("verification code is invalid or expired"));
        }

        // 查找用户
        User user = userMapper.findByEmail(request.getEmail());
        if (user == null) {
            log.warn("reset password failed: user not found, email: {}", request.getEmail());
            return ResponseEntity.badRequest().body(Result.error("user not found"));
        }

        try {
            // 更新密码（不进行加密）
            user.setPassword(request.getNewPassword());
            userMapper.updateUser(user);
            
            // 将验证码标记为已使用
            verificationCodeService.useCode(request.getEmail(), request.getCode(), RESET_PASSWORD_CODE_TYPE);
            
            log.info("password reset successfully, email: {}", request.getEmail());
            return ResponseEntity.ok(Result.success("password reset successfully"));
        } catch (Exception e) {
            log.error("reset password failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error("reset password failed, please try again later"));
        }
    }
} 