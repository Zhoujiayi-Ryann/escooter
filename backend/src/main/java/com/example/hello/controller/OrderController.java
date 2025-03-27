package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.CreateOrderRequest;
import com.example.hello.dto.OrderDetailResponse;
import com.example.hello.dto.OrderResponse;
import com.example.hello.dto.PayOrderResponse;
import com.example.hello.dto.ChangeOrderStatusResponse;
import com.example.hello.exception.OrderException;
import com.example.hello.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 * 提供订单相关的API接口
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单接口
     * 
     * @param request 创建订单请求
     * @return 创建的订单信息
     */
    @PostMapping("/create_orders")
    public Result<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            // 使用Java 8 Optional处理可能的空值
            return orderService.createOrder(request)
                    .map(response -> Result.success(response, "订单提交成功，请支付"))
                    .orElseGet(() -> Result.error("创建订单失败"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取订单详情接口
     * 
     * @param orderId 订单ID
     * @return 订单详情信息，包含滑板车信息
     */
    @GetMapping("/orders/{order_id}")
    public Result<OrderDetailResponse> getOrderDetail(@PathVariable("order_id") Integer orderId) {
        try {
            // 使用Java 8 Optional和Lambda表达式处理结果
            return orderService.getOrderDetail(orderId)
                    .map(response -> Result.success(response, "success"))
                    .orElseGet(() -> Result.error("订单不存在"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 支付订单接口
     * 将订单状态从pending更新为paid
     * 
     * @param orderId 订单ID
     * @return 支付结果
     */
    @PostMapping("/orders/{order_id}/pay")
    public Result<PayOrderResponse> payOrder(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("接收到订单支付请求: orderId={}", orderId);

            // 调用服务进行支付处理，使用Java 8 Optional处理
            return orderService.payOrder(orderId)
                    .map(response -> {
                        log.info("订单{}支付处理完成", orderId);
                        return Result.success(response, "支付成功");
                    })
                    .orElseGet(() -> {
                        log.warn("订单{}支付失败: 未返回有效结果", orderId);
                        return Result.error("支付失败: 系统错误");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("订单{}支付失败: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("订单{}支付失败: 系统异常", orderId, e);
            return Result.error("支付失败: 系统异常，请稍后重试");
        }
    }

    /**
     * 激活订单接口
     * 将订单状态从paid更新为active
     * 
     * @param orderId 订单ID
     * @return 激活结果
     */
    @PostMapping("/orders/{order_id}/activate")
    public Result<ChangeOrderStatusResponse> activateOrder(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("接收到订单激活请求: orderId={}", orderId);

            // 调用服务进行激活处理
            return orderService.activateOrder(orderId)
                    .map(response -> {
                        log.info("订单{}激活处理完成", orderId);
                        return Result.success(response, "激活成功");
                    })
                    .orElseGet(() -> {
                        log.warn("订单{}激活失败: 未返回有效结果", orderId);
                        return Result.error("激活失败: 系统错误");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("订单{}激活失败: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("订单{}激活失败: 系统异常", orderId, e);
            return Result.error("激活失败: 系统异常，请稍后重试");
        }
    }
}