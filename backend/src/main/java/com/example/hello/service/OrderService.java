package com.example.hello.service;

import com.example.hello.dto.CreateOrderRequest;
import com.example.hello.dto.OrderDetailResponse;
import com.example.hello.dto.OrderResponse;
import com.example.hello.dto.PayOrderResponse;
import java.util.Optional;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     *
     * @param request 创建订单请求
     * @return 创建的订单信息
     */
    Optional<OrderResponse> createOrder(CreateOrderRequest request);
    
    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情，包含滑板车信息
     */
    Optional<OrderDetailResponse> getOrderDetail(Integer orderId);
    
    /**
     * 支付订单
     * 将订单状态从pending更新为paid
     *
     * @param orderId 订单ID
     * @return 支付结果
     */
    Optional<PayOrderResponse> payOrder(Integer orderId);
} 