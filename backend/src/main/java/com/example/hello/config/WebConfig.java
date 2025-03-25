package com.example.hello.config;

import com.example.hello.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 配置CORS映射
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径
                .allowedOriginPatterns("*")  // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头
                .exposedHeaders("token")  // 允许客户端访问的响应头
                .allowCredentials(true)  // 允许发送cookie
                .maxAge(3600);  // 预检请求的有效期，单位为秒
    }

    /**
     * 创建CORS过滤器
     * 这是另一种CORS配置方式，它会在Spring Security之前处理请求
     * 更加可靠地解决跨域问题
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 不使用addAllowedOriginPattern("*")，而是设置AllowedOrigins列表
        // 并设置setAllowCredentials(true)
        // 或者让前端请求时带上Origin头，后端响应时动态设置Access-Control-Allow-Origin
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080", 
            "http://localhost:3000", 
            "http://localhost:8081",
            "https://aadujkrrrwxp.sealoshzh.site",
            "https://escoot-web.sealoshzh.site"
        ));
        
        config.setAllowCredentials(true);     // 允许发送cookie
        config.addAllowedMethod("*");         // 允许所有HTTP方法
        config.addAllowedHeader("*");         // 允许所有请求头
        config.addExposedHeader("token");     // 允许客户端访问的响应头
        config.setMaxAge(3600L);              // 预检请求的有效期，单位为秒

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 开发阶段临时禁用JWT拦截器
        // TODO: 开发完成后移除注释，重新启用Token验证
        /*
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有/api/路径
                .excludePathPatterns(        // 排除登录和注册接口
                        "/api/users/register",
                        "/api/users/login"
                );
        */
    }
} 