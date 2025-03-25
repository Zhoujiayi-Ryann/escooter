package com.example.hello.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 简单的CORS过滤器
 * 这个过滤器会在每个HTTP响应中添加必要的CORS头信息
 * 它的优先级设置为最高，确保在其他过滤器之前处理
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // 获取请求的Origin
        String origin = request.getHeader("Origin");

        // 添加CORS头
        // 对于允许凭证的请求，Access-Control-Allow-Origin不能为*，需要指定具体的origin
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            // 如果没有Origin头，可以设置为*
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, token, Accept, X-Requested-With, remember-me");
        response.setHeader("Access-Control-Expose-Headers", "token");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 对于OPTIONS请求（预检请求），直接返回200状态码
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // 过滤器初始化时不需要做任何事情
    }

    @Override
    public void destroy() {
        // 过滤器销毁时不需要做任何事情
    }
} 