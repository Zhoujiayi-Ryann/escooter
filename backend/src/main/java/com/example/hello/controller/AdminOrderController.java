package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.AdminTempUserOrderRequest;
import com.example.hello.dto.request.CreateOrderRequest;
import com.example.hello.dto.response.OrderResponse;
import com.example.hello.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员订单控制器
 * 提供管理员相关的订单操作API接口
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 管理员为未注册用户创建订单接口
     * 该接口用于管理员直接为未注册用户租赁车辆
     * 
     * @param request 创建订单请求
     * @return 创建的订单信息
     */
    @PostMapping("/orders/temp-user")
    public Result<OrderResponse> createOrderForTempUser(@Valid @RequestBody AdminTempUserOrderRequest request) {
        try {
            log.info("Admin creating order for temp user: adminId={}, scooterId={}, cost={}", 
                    request.getUser_id(), request.getScooter_id(), request.getCost());
            
            // 转换为创建订单请求
            CreateOrderRequest orderRequest = new CreateOrderRequest();
            orderRequest.setUser_id(request.getUser_id());
            orderRequest.setScooter_id(request.getScooter_id());
            orderRequest.setPickup_address(request.getPickup_address());
            orderRequest.setStart_time(request.getStart_time());
            orderRequest.setEnd_time(request.getEnd_time());
            
            // 使用Java 8 Stream和Optional结合处理结果
            return orderService.createOrderForTempUser(orderRequest, request.getUser_id(), request.getCost())
                    .map(response -> {
                        log.info("Order for temp user created successfully: orderId={}", response.getOrder_id());
                        return Result.success(response, "Order created successfully");
                    })
                    .orElseGet(() -> {
                        log.warn("Order creation for temp user failed: no response returned");
                        return Result.error("Order creation failed: System error");
                    });
        } catch (Exception e) {
            log.error("Admin order creation for temp user failed: {}", e.getMessage(), e);
            return Result.error("Order creation failed: " + e.getMessage());
        }
    }
} 