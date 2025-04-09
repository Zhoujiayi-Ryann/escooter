package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.CreateOrderRequest;
import com.example.hello.dto.OrderDetailResponse;
import com.example.hello.dto.OrderResponse;
import com.example.hello.dto.PayOrderResponse;
import com.example.hello.dto.ChangeOrderStatusResponse;
import com.example.hello.dto.ExtendOrderRequest;
import com.example.hello.dto.AvailableTimeSlotsResponse;
import com.example.hello.exception.OrderException;
import com.example.hello.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            log.info("接收到订单完成请求: orderId={}", orderId);

            // 调用服务进行完成处理
            return orderService.completeOrder(orderId)
                    .map(response -> {
                        log.info("订单{}完成处理完成", orderId);
                        return Result.success(response, "完成成功");
                    })
                    .orElseGet(() -> {
                        log.warn("订单{}完成失败: 未返回有效结果", orderId);
                        return Result.error("完成失败: 系统错误");
                    });
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("订单{}完成失败: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("订单{}完成失败: 系统异常", orderId, e);
            return Result.error("完成失败: 系统异常，请稍后重试");
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
            log.info("接收到订单删除请求: orderId={}", orderId);

            // 调用服务进行删除处理
            boolean deleted = orderService.deleteOrder(orderId);
            if (deleted) {
                log.info("订单{}删除成功", orderId);
                return Result.success(null, "删除成功");
            } else {
                log.warn("订单{}删除失败", orderId);
                return Result.error("删除失败");
            }
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("订单{}删除失败: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("订单{}删除失败: 系统异常", orderId, e);
            return Result.error("删除失败: 系统异常，请稍后重试");
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
            log.info("接收到订单软删除请求: orderId={}", orderId);

            // 调用服务进行软删除处理
            boolean deleted = orderService.softDeleteOrder(orderId);
            if (deleted) {
                log.info("订单{}软删除成功", orderId);
                return Result.success(null, "软删除成功");
            } else {
                log.warn("订单{}软删除失败", orderId);
                return Result.error("软删除失败");
            }
        } catch (OrderException e) {
            // 使用自定义异常处理订单相关错误
            log.warn("订单{}软删除失败: {}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的错误
            log.error("订单{}软删除失败: 系统异常", orderId, e);
            return Result.error("软删除失败: 系统异常，请稍后重试");
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
                    .orElseGet(() -> Result.error("延长订单失败"));
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
                    .orElseGet(() -> Result.error("获取可用时间段失败"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}