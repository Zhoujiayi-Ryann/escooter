package com.example.hello.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * MyBatis configuration
 */
@Configuration
@MapperScan("com.example.hello.mapper")
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 配置SqlSessionFactory，注册自定义的枚举类型处理器
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        
        // 设置MyBatis配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        
        // 注册自定义的枚举类型处理器
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        
        // 使用自定义的处理器而不是默认的EnumTypeHandler
        typeHandlerRegistry.register(com.example.hello.common.OrderStatus.class, com.example.hello.handler.OrderStatusTypeHandler.class);
        
        factoryBean.setConfiguration(configuration);
        
        // Set XML mapper location
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        
        return factoryBean.getObject();
    }
} 