package com.example.hello.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.TimeZone;
import java.time.ZoneId;

import lombok.extern.slf4j.Slf4j;

/**
 * 时区配置类
 * 确保整个应用程序统一使用中国时区
 */
@Configuration
@Slf4j
public class TimeZoneConfig implements ApplicationListener<ContextRefreshedEvent> {

    private static final String CHINA_TIME_ZONE = "Asia/Shanghai";

    /**
     * 应用启动时设置默认时区
     * 使用Spring的事件监听器代替@PostConstruct
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 设置JVM默认时区
        TimeZone.setDefault(TimeZone.getTimeZone(CHINA_TIME_ZONE));
        log.info("Application timezone set to: {}", TimeZone.getDefault().getID());
    }

    /**
     * 应用启动完成后检查时区配置
     */
    @EventListener(ApplicationReadyEvent.class)
    public void checkTimeZoneConfig() {
        // 检查JVM时区
        log.info("Current JVM timezone: {}", TimeZone.getDefault().getID());
        
        // 检查Java 8 time API时区
        log.info("Current Java 8 time API timezone: {}", ZoneId.systemDefault());
        
        // 当前时间（确认时区生效）
        log.info("Current time in system timezone: {}", java.time.LocalDateTime.now());
        log.info("Current time in Asia/Shanghai: {}", java.time.LocalDateTime.now(ZoneId.of(CHINA_TIME_ZONE)));
    }
} 