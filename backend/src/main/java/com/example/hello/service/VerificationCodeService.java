package com.example.hello.service;

/**
 * 验证码服务接口
 */
public interface VerificationCodeService {

    /**
     * 生成验证码并发送到用户邮箱
     * @param email 用户邮箱
     * @param type 验证码类型 (如: "REGISTER", "RESET_PASSWORD")
     * @return 生成的验证码
     */
    String generateAndSendCode(String email, String type);

    /**
     * 验证用户提供的验证码是否有效
     * @param email 用户邮箱
     * @param code 用户提供的验证码
     * @param type 验证码类型
     * @return 验证结果 (true: 验证成功, false: 验证失败)
     */
    boolean verifyCode(String email, String code, String type);

    /**
     * 将验证码标记为已使用
     * @param email 用户邮箱
     * @param code 验证码
     * @param type 验证码类型
     * @return 操作结果 (true: 成功, false: 失败)
     */
    boolean useCode(String email, String code, String type);

    /**
     * 使指定用户的所有特定类型验证码失效
     * @param email 用户邮箱
     * @param type 验证码类型
     * @return 操作结果 (true: 成功, false: 失败)
     */
    boolean invalidateAllCodes(String email, String type);
} 