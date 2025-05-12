package com.example.hello.config;

import com.example.hello.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Value("${app.upload.dir:${user.dir}/src/main/resources/static/feedback}")
    private String uploadDir;

    @Value("${app.avatar.upload.dir}")
    private String avatarUploadDir;

    @Value("${app.virtual-path.avatar}")
    private String avatarVirtualPath;

    @Value("${app.virtual-path.feedback}")
    private String feedbackVirtualPath;

    /**
     * 配置CORS映射
     */
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOriginPatterns(
    //                 "http://localhost:*",
    //                 "http://127.0.0.1:*",
    //                 "https://*.sealoshzh.site",
    //                 "http://139.155.11.34",
    //                 "https://139.155.11.34"
    //             )
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
    //             .allowedHeaders("*")
    //             .exposedHeaders("token")
    //             .allowCredentials(true)
    //             .maxAge(3600);
    // }

    /**
     * 创建CORS过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的域名模式
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://*.sealoshzh.site",
            "http://139.155.11.34",
            "https://139.155.11.34"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("token");
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    /**
     * 添加静态资源处理器，映射上传文件访问路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置反馈图片访问路径
        registry.addResourceHandler(feedbackVirtualPath + "/**")
                .addResourceLocations("file:" + uploadDir + "/images/");
                
        // 配置头像访问路径
        registry.addResourceHandler(avatarVirtualPath + "/**")
                .addResourceLocations("file:" + avatarUploadDir + "/");
                
        // 配置静态资源访问路径
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 开发阶段临时禁用JWT拦截器
        // TODO: 开发完成后移除注释，重新启用Token验证
        /*
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/users/register",
                        "/api/users/login"
                );
        */
    }
} 