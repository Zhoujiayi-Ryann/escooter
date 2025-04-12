package com.example.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class HelloApplication {
    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                logger.info("Test database connection...");
                String result = jdbcTemplate.queryForObject("SELECT 'Connection successful'", String.class);
                logger.info("Test database connection successful: {}", result);
            } catch (Exception e) {
                logger.error("Test database connection failed: {}", e.getMessage(), e);
                // 不终止应用程序，让它继续尝试运行
                // 在某些环境中，初始连接可能失败，但随后的连接可能成功
            }
        };
    }
}
