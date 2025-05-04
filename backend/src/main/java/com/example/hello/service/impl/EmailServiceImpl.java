package com.example.hello.service.impl;

import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.mapper.ScooterMapper;
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
            // 添加用户名变量 - 从订单用户ID获取用户名，如果无法获取则使用"Customer"
            context.setVariable("userName", "Customer"); // 默认值，如果有用户服务可以获取实际用户名
            
            // 尝试获取用户名（这里仅作示例，实际实现应该调用用户服务）
            try {
                // 这里可以添加获取用户名的代码
                // 例如: String username = userService.getUserById(order.getUserId()).getUsername();
                // 如果能获取到用户名，则设置为真实用户名
                // context.setVariable("userName", username);
            } catch (Exception e) {
                log.warn("Could not retrieve username for userId={}, using default", order.getUserId());
                // 使用默认值继续执行，不中断邮件发送
            }
            
            context.setVariable("orderId", order.getOrderId());
            context.setVariable("startTime", order.getStartTime().format(DATE_TIME_FORMATTER));
            context.setVariable("endTime", order.getEndTime().format(DATE_TIME_FORMATTER));
            context.setVariable("duration", order.getDuration());
            context.setVariable("address", order.getAddress());
            context.setVariable("scooterId", scooter.getScooterId());
            context.setVariable("batteryLevel", scooter.getBatteryLevel());
            
            // 计算原价和折扣
            BigDecimal originalCost = order.getCost().add(order.getDiscount());
            context.setVariable("originalCost", originalCost);
            context.setVariable("discount", order.getDiscount());
            context.setVariable("finalCost", order.getCost());
            
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
                    MAX_RETRIES, order.getOrderId(), to, previousException.getMessage());
            return;
        }
        
        try {
            // 退避策略 - 每次重试等待时间递增
            Thread.sleep(1000 * retryCount);
            log.info("Retrying to send order confirmation email, attempt {}/{}: orderId={}, recipient={}",
                    retryCount, MAX_RETRIES, order.getOrderId(), to);
            sendOrderConfirmationEmail(order, to);
        } catch (Exception e) {
            log.warn("Retry {} failed: {}", retryCount, e.getMessage());
            retryOrderConfirmationEmail(order, to, retryCount + 1, e);
        }
    }
}