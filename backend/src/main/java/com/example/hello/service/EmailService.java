package com.example.hello.service;

import com.example.hello.entity.Order;

/**
 * 邮件服务接口
 * 提供发送各种类型邮件的功能
 */
public interface EmailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送HTML格式邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content HTML格式内容
     */
    void sendHtmlEmail(String to, String subject, String content);

    /**
     * 发送订单支付成功确认邮件
     *
     * @param order 订单信息
     * @param to    收件人邮箱
     */
    void sendOrderConfirmationEmail(Order order, String to);
}