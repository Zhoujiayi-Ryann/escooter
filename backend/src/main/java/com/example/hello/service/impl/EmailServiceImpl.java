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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("HTML email sent successfully, recipient: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email: {}", e.getMessage(), e);
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
        }
    }
}