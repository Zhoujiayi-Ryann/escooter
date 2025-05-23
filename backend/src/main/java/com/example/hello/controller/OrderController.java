package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.CreateOrderRequest;
import com.example.hello.dto.response.OrderDetailResponse;
import com.example.hello.dto.response.OrderResponse;
import com.example.hello.dto.response.PayOrderResponse;
import com.example.hello.dto.response.ChangeOrderStatusResponse;
import com.example.hello.dto.request.ExtendOrderRequest;
import com.example.hello.dto.response.AvailableTimeSlotsResponse;
import com.example.hello.dto.response.AvailableCouponsResponse;
import com.example.hello.dto.request.CouponRequest;
import com.example.hello.dto.response.RevenueStatisticsResponse;
import com.example.hello.exception.OrderException;
import com.example.hello.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

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
                    .map(response -> Result.success(response, "Order created successfully, please continue to pay"))
                    .orElseGet(() -> Result.error("Order creation failed"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取订单详情接口（支付时使用）
     * 
     * @param orderId 订单ID
     * @return 订单详情信息，包含滑板车信息
     */
    @GetMapping("/orders/{order_id}/rent")
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
     * @param orderId       订单ID
     * @param couponRequest 优惠券请求（可选）
     * @return 支付结果
     */
    @PostMapping("/orders/{order_id}/pay")
    public Result<PayOrderResponse> payOrder(
            @PathVariable("order_id") Integer orderId,
            @RequestBody(required = false) CouponRequest couponRequest) {
        try {
            log.info("Received order payment request: orderId={}, couponRequest={}", orderId, couponRequest);

            // 调用服务进行支付处理，使用Java 8 Optional处理
            return orderService.payOrder(orderId, couponRequest)
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

    /**
     * 获取用户所有未使用的优惠券
     * 
     * @param userId 用户ID
     * @return 优惠券列表
     */
    @GetMapping("/users/{user_id}/coupons")
    public Result<AvailableCouponsResponse> getAvailableCoupons(
            @PathVariable("user_id") Integer userId) {
        try {
            log.info("Getting available coupons for user: userId={}", userId);

            Optional<AvailableCouponsResponse> response = orderService.getAvailableCoupons(userId);
            return response.map(r -> Result.success(r, "success"))
                    .orElseGet(() -> Result.error("No available coupons"));
        } catch (OrderException e) {
            log.warn("Failed to get available coupons: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to get available coupons: system error", e);
            return Result.error("Failed to get available coupons: system error");
        }
    }

    /**
     * 获取订单所有信息接口
     * 不同于getOrderDetail，此接口用于通用业务查询，不涉及支付流程
     * 只返回数据库里的原始信息，不进行价格计算等额外处理
     * 
     * @param orderId 订单ID
     * @return 订单所有信息
     */
    @GetMapping("/orders/{order_id}")
    public Result<OrderDetailResponse> getOrderInfo(@PathVariable("order_id") Integer orderId) {
        try {
            log.info("Getting raw order information: orderId={}", orderId);

            return orderService.getOrderRawInfo(orderId)
                    .map(response -> Result.success(response, "Order information retrieved successfully"))
                    .orElseGet(() -> Result.error("Order does not exist"));
        } catch (OrderException e) {
            log.warn("Failed to get order information: orderId={}, error={}", orderId, e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to get order information: orderId={}, error={}", orderId, e);
            return Result.error("Failed to get order information: System error");
        }
    }

    /**
     * 获取所有订单
     * 
     * @return 所有订单列表
     */
    @GetMapping("/orders")
    public Result<List<OrderResponse>> getAllOrders() {
        try {
            log.info("Getting all orders");
            List<OrderResponse> orders = orderService.getAllOrders();
            return Result.success(orders, "success");
        } catch (OrderException e) {
            log.warn("Failed to get all orders: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to get all orders: system error", e);
            return Result.error("Failed to get all orders: System error");
        }
    }

    /**
     * 获取系统总收入
     * 
     * @return 系统总收入
     */
    @GetMapping("/total-revenue")
    public Result<BigDecimal> getTotalRevenue() {
        try {
            BigDecimal totalRevenue = orderService.getTotalRevenue();
            return Result.success(totalRevenue, "Get total revenue successfully");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/daily-revenue")
    public Result<BigDecimal> getDailyRevenue() {
        try {
            BigDecimal dailyRevenue = orderService.getDailyRevenue();
            return Result.success(dailyRevenue, "Get daily revenue successfully");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/revenue-by-date-range")
    public Result<BigDecimal> getRevenueByDateRange(
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate) {
        try {
            log.info("Received revenue request for date range: {} to {}", startDate, endDate);
            BigDecimal revenue = orderService.getRevenueByDateRange(startDate, endDate);
            return Result.success(revenue, "Get revenue by date range successfully");
        } catch (Exception e) {
            log.error("Failed to get revenue by date range: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/revenue-statistics")
    public Result<RevenueStatisticsResponse> getRevenueStatistics(
            @RequestParam(value = "start_date", required = true) String startDate,
            @RequestParam(value = "end_date", required = true) String endDate) {
        try {
            log.info("Received revenue statistics request for date range: {} to {}", startDate, endDate);

            // 验证日期参数
            if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
                return Result.error("Start date and end date cannot be empty");
            }

            // 验证日期格式
            try {
                java.time.LocalDate.parse(startDate);
                java.time.LocalDate.parse(endDate);
            } catch (Exception e) {
                return Result.error("Invalid date format, please use yyyy-MM-dd format");
            }

            RevenueStatisticsResponse statistics = orderService.getRevenueStatistics(startDate, endDate);
            return Result.success(statistics, "Get revenue statistics successfully");
        } catch (Exception e) {
            log.error("Failed to get revenue statistics: {}", e.getMessage());
            return Result.error("Failed to get revenue statistics: " + e.getMessage());
        }
    }

    @GetMapping("/total-order-count")
    public ResponseEntity<Map<String, Object>> getTotalOrderCount() {
        log.info("Getting total order count");
        try {
            int count = orderService.getTotalOrderCount();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("msg", "Get total order count successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get total order count", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("msg", "Failed to get total order count");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}