package com.example.hello.service.impl;

import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.entity.User;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.mapper.UserMapper;
import com.example.hello.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 邮件服务实现类
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ScooterMapper scooterMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String sender;
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            log.info("Simple email sent successfully, recipient: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String content) {
        try {
            log.info("Preparing to send HTML email to {}, subject: {}", to, subject);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            log.debug("Email content prepared, attempting to send...");
            mailSender.send(message);
            log.info("HTML email sent successfully, recipient: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {} - {}", to, e.getClass().getName(), e.getMessage(), e);
            log.debug("Email sending failed with the following details: Subject: '{}', Sender: '{}'", subject, sender);
            if (e.getCause() != null) {
                log.error("Root cause: {}", e.getCause().getMessage());
            }
            
            // 检查常见错误原因
            if (e.getMessage().contains("connect timed out") || e.getMessage().contains("Connection timed out")) {
                log.error("SMTP server connection timed out. Check network connectivity and firewall settings.");
            } else if (e.getMessage().contains("Invalid Addresses")) {
                log.error("Invalid email address: {}", to);
            } else if (e.getMessage().contains("Authentication failed")) {
                log.error("SMTP authentication failed. Check username and password in application.properties.");
            } else if (e.getMessage().contains("Could not connect to SMTP host")) {
                log.error("Could not connect to SMTP host. Check if SMTP server is accessible and port is open.");
            }
        } catch (Exception e) {
            log.error("Unexpected error while sending HTML email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendOrderConfirmationEmail(Order order, String to) {
        try {
            // 参数验证
            if (order == null) {
                log.error("Failed to send order confirmation email: Order object is null");
                return;
            }
            
            if (to == null || to.trim().isEmpty()) {
                log.error("Failed to send order confirmation email: Recipient email is null or empty");
                return;
            }
            
            // 检查必要字段
            if (order.getOrderId() == null) {
                log.error("Failed to send order confirmation email: Order ID is null");
                return;
            }
            
            if (order.getScooterId() == null) {
                log.error("Failed to send order confirmation email: Scooter ID is null, orderId={}", order.getOrderId());
                return;
            }
            
            // 获取滑板车信息
            Scooter scooter = scooterMapper.findById(order.getScooterId());
            if (scooter == null) {
                log.error("Failed to send order confirmation email: Scooter information not found, scooterId={}", order.getScooterId());
                return;
            }
            
            // 构建邮件主题
            String subject = "Order payment successful - Order number: " + order.getOrderId();
            
            // 准备模板变量
            Context context = new Context(Locale.US);
            
            // 获取实际用户名
            String userName = "Customer"; // 默认值
            try {
                if (order.getUserId() != null) {
                    User user = userMapper.findById(order.getUserId().longValue());
                    if (user != null && user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                        userName = user.getUsername();
                        log.info("Found username: {} for userId: {}", userName, order.getUserId());
                    } else {
                        log.warn("User not found or username is empty for userId: {}", order.getUserId());
                    }
                } else {
                    log.warn("User ID is null in order: {}", order.getOrderId());
                }
            } catch (Exception e) {
                log.warn("Failed to get username for userId: {}, error: {}", order.getUserId(), e.getMessage());
                // 使用默认值继续执行，不中断邮件发送
            }
            
            // 设置用户名变量
            context.setVariable("userName", userName);
            
            // 订单基本信息
            context.setVariable("orderId", order.getOrderId());
            
            // 安全处理时间格式
            if (order.getStartTime() != null) {
                context.setVariable("startTime", order.getStartTime().format(DATE_TIME_FORMATTER));
            } else {
                context.setVariable("startTime", "Not specified");
            }
            
            if (order.getEndTime() != null) {
                context.setVariable("endTime", order.getEndTime().format(DATE_TIME_FORMATTER));
            } else {
                context.setVariable("endTime", "Not specified");
            }
            
            // 安全处理其他字段
            context.setVariable("duration", order.getDuration() != null ? order.getDuration() : 0);
            context.setVariable("address", order.getAddress() != null ? order.getAddress() : "Not specified");
            
            // 滑板车信息
            context.setVariable("scooterId", scooter.getScooterId() != null ? scooter.getScooterId() : "Unknown");
            context.setVariable("batteryLevel", scooter.getBatteryLevel() != null ? scooter.getBatteryLevel() : 0);
            
            // 计算原价和折扣，安全处理null值
            BigDecimal cost = order.getCost() != null ? order.getCost() : BigDecimal.ZERO;
            BigDecimal discount = order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO;
            BigDecimal originalCost = cost.add(discount);
            
            context.setVariable("originalCost", originalCost);
            context.setVariable("discount", discount);
            context.setVariable("finalCost", cost);
            
            // 使用模板引擎处理邮件模板
            String emailContent = templateEngine.process("mail/order-confirmation", context);
            
            // 发送HTML邮件
            sendHtmlEmail(to, subject, emailContent);
            log.info("Order confirmation email sent successfully, orderId: {}, recipient: {}", order.getOrderId(), to);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email: {}", e.getMessage(), e);
            // 增加重试机制
            retryOrderConfirmationEmail(order, to, 1, e);
        }
    }
    
    /**
     * 重试发送订单确认邮件
     * 
     * @param order 订单信息
     * @param to 收件人
     * @param retryCount 当前重试次数
     * @param previousException 上一次异常
     */
    private void retryOrderConfirmationEmail(Order order, String to, int retryCount, Exception previousException) {
        // 最多重试3次
        final int MAX_RETRIES = 3;
        if (retryCount > MAX_RETRIES) {
            log.error("Failed to send order confirmation email after {} retries: orderId={}, recipient={}, error={}",
                    MAX_RETRIES, order != null ? order.getOrderId() : "null", to, previousException.getMessage());
            return;
        }
        
        try {
            // 参数验证
            if (order == null) {
                log.error("Failed to retry sending email: Order object is null");
                return;
            }
            
            if (to == null || to.trim().isEmpty()) {
                log.error("Failed to retry sending email: Recipient email is null or empty");
                return;
            }
            
            // 退避策略 - 每次重试等待时间递增
            Thread.sleep(1000 * retryCount);
            log.info("Retrying to send order confirmation email, attempt {}/{}: orderId={}, recipient={}",
                    retryCount, MAX_RETRIES, order.getOrderId(), to);
            
            // 直接在这里重新实现邮件发送逻辑，避免递归调用sendOrderConfirmationEmail方法
            // 检查必要字段
            if (order.getOrderId() == null) {
                log.error("Failed to retry sending email: Order ID is null");
                return;
            }
            
            if (order.getScooterId() == null) {
                log.error("Failed to retry sending email: Scooter ID is null, orderId={}", order.getOrderId());
                return;
            }
            
            // 获取滑板车信息
            Scooter scooter = scooterMapper.findById(order.getScooterId());
            if (scooter == null) {
                log.error("Failed to retry email: Scooter information not found, scooterId={}", order.getScooterId());
                return;
            }
            
            // 构建邮件主题
            String subject = "Order payment successful - Order number: " + order.getOrderId();
            
            // 准备模板变量
            Context context = new Context(Locale.US);
            
            // 获取实际用户名
            String userName = "Customer"; // 默认值
            try {
                if (order.getUserId() != null) {
                    User user = userMapper.findById(order.getUserId().longValue());
                    if (user != null && user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                        userName = user.getUsername();
                        log.info("Retry found username: {} for userId: {}", userName, order.getUserId());
                    } else {
                        log.warn("Retry: User not found or username is empty for userId: {}", order.getUserId());
                    }
                } else {
                    log.warn("Retry: User ID is null in order: {}", order.getOrderId());
                }
            } catch (Exception e) {
                log.warn("Retry: Failed to get username for userId: {}, error: {}", order.getUserId(), e.getMessage());
                // 使用默认值继续
            }
            
            // 设置用户名变量
            context.setVariable("userName", userName);
            context.setVariable("orderId", order.getOrderId());
            
            // 安全处理时间格式
            if (order.getStartTime() != null) {
                context.setVariable("startTime", order.getStartTime().format(DATE_TIME_FORMATTER));
            } else {
                context.setVariable("startTime", "Not specified");
            }
            
            if (order.getEndTime() != null) {
                context.setVariable("endTime", order.getEndTime().format(DATE_TIME_FORMATTER));
            } else {
                context.setVariable("endTime", "Not specified");
            }
            
            // 安全处理其他字段
            context.setVariable("duration", order.getDuration() != null ? order.getDuration() : 0);
            context.setVariable("address", order.getAddress() != null ? order.getAddress() : "Not specified");
            
            // 滑板车信息
            context.setVariable("scooterId", scooter.getScooterId() != null ? scooter.getScooterId() : "Unknown");
            context.setVariable("batteryLevel", scooter.getBatteryLevel() != null ? scooter.getBatteryLevel() : 0);
            
            // 计算原价和折扣，安全处理null值
            BigDecimal cost = order.getCost() != null ? order.getCost() : BigDecimal.ZERO;
            BigDecimal discount = order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO;
            BigDecimal originalCost = cost.add(discount);
            
            context.setVariable("originalCost", originalCost);
            context.setVariable("discount", discount);
            context.setVariable("finalCost", cost);
            
            // 使用模板引擎处理邮件模板
            String emailContent = templateEngine.process("mail/order-confirmation", context);
            
            // 发送HTML邮件
            sendHtmlEmail(to, subject, emailContent);
            log.info("Retry successful: Order confirmation email sent, orderId: {}, recipient: {}", order.getOrderId(), to);
            
        } catch (Exception e) {
            log.warn("Retry {} failed: {}", retryCount, e.getMessage());
            // 增加重试次数并继续重试
            if (retryCount < MAX_RETRIES) {
                try {
                    Thread.sleep(1000 * retryCount); // 重试前等待时间
                    retryOrderConfirmationEmail(order, to, retryCount + 1, e);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Retry interrupted: {}", ie.getMessage());
                }
            }
        }
    }
}