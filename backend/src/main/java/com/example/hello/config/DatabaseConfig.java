package com.example.hello.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 数据库配置类
 * 提供自定义数据源配置，增强连接池的稳定性和性能
 * 包含自动网络检测和备用连接尝试功能
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
    
    @Autowired
    private Environment environment;
    
    // 检查当前激活的配置文件
    private String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }
    
    // 检查是否在开发环境中
    private boolean isDevEnvironment() {
        return Arrays.asList(getActiveProfiles()).contains("dev");
    }
    
    // 从URL中提取主机名，用于网络诊断
    private String extractHostFromJdbcUrl(String jdbcUrl) {
        try {
            // 从JDBC URL中提取主机部分
            // 格式: jdbc:mysql://hostname:port/database
            String hostPart = jdbcUrl.split("//")[1].split("/")[0];
            return hostPart.contains(":") ? hostPart.split(":")[0] : hostPart;
        } catch (Exception e) {
            logger.error("无法从JDBC URL提取主机名: {}", jdbcUrl, e);
            return null;
        }
    }

    // 诊断网络连接
    private boolean diagnoseNetworkConnection() {
        String host = extractHostFromJdbcUrl(url);
        if (host != null) {
            try {
                logger.info("尝试解析数据库主机: {}", host);
                InetAddress address = InetAddress.getByName(host);
                logger.info("数据库主机解析结果: {} -> {}", host, address.getHostAddress());
                
                // 尝试ping检测（简单实现）
                boolean reachable = address.isReachable(5000);
                logger.info("数据库主机是否可达: {}", reachable);
                return reachable;
            } catch (UnknownHostException e) {
                logger.error("无法解析数据库主机: {}", host, e);
                return false;
            } catch (Exception e) {
                logger.error("网络诊断失败: {}", e.getMessage(), e);
                return false;
            }
        }
        return false;
    }
    
    // 输出环境诊断信息
    private void logEnvironmentInfo() {
        logger.info("当前激活的配置文件: {}", Arrays.toString(getActiveProfiles()));
        logger.info("是否为开发环境: {}", isDevEnvironment());
        logger.info("使用的数据库URL: {}", url);
        
        // 尝试获取本机网络信息
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            logger.info("本机主机名: {}", localHost.getHostName());
            logger.info("本机IP地址: {}", localHost.getHostAddress());
        } catch (UnknownHostException e) {
            logger.error("无法获取本机网络信息", e);
        }
        
        // 检查Java系统属性
        logger.info("Java版本: {}", System.getProperty("java.version"));
        logger.info("操作系统: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        // 输出环境诊断信息
        logEnvironmentInfo();
        
        logger.info("初始化数据源，URL: {}", url);
        
        // 进行网络诊断
        boolean networkAvailable = diagnoseNetworkConnection();
        if (!networkAvailable) {
            logger.warn("数据库网络连接诊断失败，可能无法连接到数据库服务器");
            logger.warn("如果您在集群外运行此应用，请考虑以下解决方案：");
            logger.warn("1. 使用VPN连接到集群网络");
            logger.warn("2. 配置SSH隧道到集群内数据库");
            logger.warn("3. 使用 -Dspring.profiles.active=dev 启动应用，并确保配置了开发环境的数据库连接");
        }
        
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
        dataSource.setKeepaliveTime(60000); // 60秒保活
        
        // 自动重连设置
        dataSource.setAutoCommit(true);
        dataSource.setConnectionInitSql("SET NAMES utf8mb4");
        
        // 泄漏检测
        dataSource.setLeakDetectionThreshold(60000); // 60秒后检测泄漏
        
        // 最大生命周期，避免连接太久不刷新
        dataSource.setMaxLifetime(1800000); // 30分钟
        
        logger.info("数据源初始化完成: {}", dataSource);
        return dataSource;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        logger.info("创建事务管理器");
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    /**
     * 定期测试数据库连接
     * 每10分钟执行一次，确保连接池中的连接有效
     */
    @Scheduled(fixedRate = 600000)
    public void checkDatabaseConnection() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
            String result = jdbcTemplate.queryForObject("SELECT 'Connection OK'", String.class);
            logger.info("数据库连接检查: {}", result);
        } catch (DataAccessException e) {
            logger.error("数据库连接检查失败", e);
            // 记录详细错误信息，帮助诊断问题
            if (e.getCause() instanceof SQLException) {
                SQLException sqlException = (SQLException) e.getCause();
                logger.error("SQL异常: 状态码={}, 错误码={}, 消息={}", 
                    sqlException.getSQLState(), 
                    sqlException.getErrorCode(), 
                    sqlException.getMessage());
            }
            // 尝试进行网络诊断
            diagnoseNetworkConnection();
        }
    }
} 