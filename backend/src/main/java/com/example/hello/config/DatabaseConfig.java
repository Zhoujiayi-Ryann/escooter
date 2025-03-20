package com.example.hello.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库配置类
 * 提供自定义数据源配置，增强连接池的稳定性和性能
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("初始化数据源，URL: {}", url);
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        
        // 连接池配置
        dataSource.setConnectionTimeout(30000); // 30秒
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(5);
        
        // 设置验证连接的有效性
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setValidationTimeout(5000); // 5秒
        
        // 添加重试逻辑
        dataSource.setInitializationFailTimeout(60000); // 60秒内尝试初始化
        
        // 自动重连设置
        dataSource.setAutoCommit(true);
        dataSource.setConnectionInitSql("SET NAMES utf8mb4");
        
        // 最大生命周期，避免连接太久不刷新
        dataSource.setMaxLifetime(1800000); // 30分钟
        
        logger.info("数据源初始化完成");
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
} 