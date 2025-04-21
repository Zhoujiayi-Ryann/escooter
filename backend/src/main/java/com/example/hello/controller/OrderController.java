package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.CreateOrderRequest;
import com.example.hello.dto.response.OrderDetailResponse;
import com.example.hello.dto.response.OrderResponse;
import com.example.hello.dto.response.PayOrderResponse;
import com.example.hello.dto.response.ChangeOrderStatusResponse;
import com.example.hello.dto.request.ExtendOrderRequest;
import com.example.hello.dto.response.AvailableTimeSlotsResponse;
import com.example.hello.exception.OrderException;
import com.example.hello.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                    .map(response -> Result.success(response, "订单创建成功，请继续完成支付"))
                    .orElseGet(() -> Result.error("订单创建失败"));
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
                    .orElseGet(() -> Result.error("Order does not exist"));
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
            log.info("Received order payment request: orderId={}", orderId);

            // 调用服务进行支付处理，使用Java 8 Optional处理
            return orderService.payOrder(orderId)
                    .map(response -> {
                        log.info("Order {} payment processing completed", orderId);
                        return Result.success(response, "Payment successful");
                    })
                    .orElseGet(() -> {
                        log.warn("Order {} payment failed: no valid result", orderId);
                        return Result.error("Payment failed: System error");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("Order {} payment failed: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("Order {} payment failed: {}", orderId, e);
            return Result.error("Payment failed: System error");
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
            log.info("Received order activation request: orderId={}", orderId);

            // 调用服务进行激活处理
            return orderService.activateOrder(orderId)
                    .map(response -> {
                        log.info("Order {} activation processing completed", orderId);
                        return Result.success(response, "Activation successful");
                    })
                    .orElseGet(() -> {
                        log.warn("Order {} activation failed: no valid result", orderId);
                        return Result.error("Activation failed: System error");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("Order {} activation failed:{}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("Order {} activation failed: {}", orderId, e);
            return Result.error("Activation failed: System error");
        }
    }

    /**
     * 完成订单接口
     * 将订单状态从active更新为completed
     * 
     * @param orderId 订单ID
     * @return 完成结果
     */
    @PostMapping("/orders/{order_id}/complete")
    public Result<ChangeOrderStatusResponse> completeOrder(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("Received order completion request: orderId={}", orderId);

            // 调用服务进行完成处理
            return orderService.completeOrder(orderId)
                    .map(response -> {
                        log.info("Order {} completion processing completed", orderId);
                        return Result.success(response, "Completion successful");
                    })
                    .orElseGet(() -> {
                        log.warn("Order {} completion failed: no valid result", orderId);
                        return Result.error("Completion failed: System error");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("Order {} completion failed: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("Order {} completion failed: {}", orderId, e);
            return Result.error("Completion failed: System error");
        }
    }

    /**
     * 删除未支付订单接口
     * 只能删除状态为pending的订单
     * 
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/orders/{order_id}")
    public Result<Void> deleteOrder(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("Received order deletion request: orderId={}", orderId);

            // 调用服务进行删除处理
            boolean deleted = orderService.deleteOrder(orderId);
            if (deleted) {
                log.info("Order {} delete successfully", orderId);
                return Result.success(null, "Delete successfully");
            } else {
                log.warn("Order {} delete failed", orderId);
                return Result.error("Delete failed");
            }
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("Order {} delete failed: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("Order {} delete failed: system error", orderId, e);
            return Result.error("Delete failed: System error");
        }
    }

    /**
     * 软删除已完成的订单接口
     * 只能删除状态为completed的订单
     * 
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/orders/{order_id}/soft")
    public Result<Void> softDeleteOrder(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("Received order soft delete request: orderId={}", orderId);

            // 调用服务进行软删除处理
            boolean deleted = orderService.softDeleteOrder(orderId);
            if (deleted) {
                log.info("Order {} soft delete successfully", orderId);
                return Result.success(null, "Soft delete successfully");
            } else {
                log.warn("Order {} soft delete failed", orderId);
                return Result.error("Soft delete failed");
            }
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("Order {} soft delete failed: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("Order {} soft delete failed: system failed", orderId, e);
            return Result.error("Soft delete failed: System error");
        }
    }

    /**
     * 延长订单时间
     * 
     * @param request 延长订单请求
     * @return 更新后的订单信息
     */
    @PostMapping("/orders/{order_id}/extend")
    public Result<OrderResponse> extendOrder(@PathVariable("order_id") Integer orderId,
            @RequestBody ExtendOrderRequest request) {
        try {
            request.setOrder_id(orderId); // 设置订单ID
            Optional<OrderResponse> response = orderService.extendOrder(request);
            return response.map(Result::success)
                    .orElseGet(() -> Result.error("Extend order failed"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取订单可延长的时间段
     * 
     * @param orderId 订单ID
     * @return 可用时间段信息
     */
    @GetMapping("/orders/{order_id}/available-slots")
    public Result<AvailableTimeSlotsResponse> getAvailableTimeSlots(@PathVariable("order_id") Integer orderId) {
        try {
            Optional<AvailableTimeSlotsResponse> response = orderService.getAvailableTimeSlots(orderId);
            return response.map(Result::success)
                    .orElseGet(() -> Result.error("Failed to get available time slots"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户的所有订单接口
     * 按创建时间倒序排序
     * 
     * @param userId 用户ID
     * @return 订单列表
     */
    @GetMapping("/users/{user_id}/orders")
    public Result<List<OrderResponse>> getUserOrders(@PathVariable("user_id") Integer userId) {
        try {
            log.info("Getting orders for user: {}", userId);
            List<OrderResponse> orders = orderService.getUserOrders(userId);
            return Result.success(orders, "success");
        } catch (OrderException e) {
            log.warn("Failed to get orders for user {}: {}", userId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to get orders for user {}: system error", userId, e);
            return Result.error("Failed to get user orders: System error");
        }
    }
}