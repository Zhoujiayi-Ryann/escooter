// package com.example.hello.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsFilter corsFilter() {
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
        
//         // 允许cookies等凭证
//         config.setAllowCredentials(true);
        
//         // 使用通配符允许所有来源
//         // 注意：当allowCredentials为true时，不能使用"*"，但可以使用"**"或正则匹配
//         config.addAllowedOriginPattern("*");
        
//         // 允许的HTTP方法
//         config.addAllowedMethod("GET");
//         config.addAllowedMethod("POST");
//         config.addAllowedMethod("PUT");
//         config.addAllowedMethod("DELETE");
//         config.addAllowedMethod("OPTIONS");
        
//         // 允许所有请求头
//         config.addAllowedHeader("*");
        
//         // 暴露token头
//         config.addExposedHeader("token");
        
//         // 设置预检请求的缓存时间
//         config.setMaxAge(3600L);
        
//         // 对所有API路径应用CORS配置
//         source.registerCorsConfiguration("/**", config);
        
//         return new CorsFilter(source);
//     }
// } 