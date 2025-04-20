package com.example.hello.service.impl;

import com.example.hello.common.OrderStatus;
import com.example.hello.dto.request.CreateOrderRequest;
import com.example.hello.dto.response.OrderDetailResponse;
import com.example.hello.dto.response.OrderResponse;
import com.example.hello.dto.response.PayOrderResponse;
import com.example.hello.dto.response.ChangeOrderStatusResponse;
import com.example.hello.dto.request.ExtendOrderRequest;
import com.example.hello.dto.response.AvailableTimeSlotsResponse;
import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.exception.OrderException;
import com.example.hello.mapper.OrderMapper;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.mapper.DiscountMapper;
import com.example.hello.service.OrderService;
import com.example.hello.service.EmailService;
import com.example.hello.entity.User;
import com.example.hello.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订单服务实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ScooterMapper scooterMapper;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    /**
     * 创建订单
     * 使用事务确保数据一致性
     *
     * @param request 创建订单请求
     * @return 创建的订单信息
     */
    @Override
    @Transactional
    public Optional<OrderResponse> createOrder(CreateOrderRequest request) {
        log.info("Starting create order: userId={}, scooterId={}", request.getUser_id(), request.getScooter_id());

        // 1. 检查滑板车是否存在
        Scooter scooter = scooterMapper.findById(request.getScooter_id());
        if (scooter == null) {
            log.error("Scooter does not exist: scooterId={}", request.getScooter_id());
            throw new RuntimeException("滑板车不存在");
        }

        // 2. 验证开始时间和结束时间
        if (request.getEnd_time().isBefore(request.getStart_time())) {
            log.error("End time cannot be earlier than start time: startTime={}, endTime={}", request.getStart_time(),
                    request.getEnd_time());
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        // 3. 检查时间段是否与其他订单重叠
        List<Order> overlappingOrders = orderMapper.findOverlappingOrders(
                request.getScooter_id(),
                request.getStart_time(),
                request.getEnd_time());
        if (!overlappingOrders.isEmpty()) {
            log.error("Scooter already booked during this time period: scooterId={}, startTime={}, endTime={}",
                    request.getScooter_id(), request.getStart_time(), request.getEnd_time());
            throw new RuntimeException("该时间段内滑板车已被预订");
        }

        // 4. 计算使用时长（小时）
        Duration duration = Duration.between(request.getStart_time(), request.getEnd_time());
        float durationHours = duration.toMinutes() / 60.0f;
        log.info("Rental duration: {} hours", durationHours);

        // 5. 获取用户折扣率（如果有）
        BigDecimal discountRate = discountMapper.getUserDiscountRate(request.getUser_id());
        if (discountRate == null) {
            discountRate = BigDecimal.ONE; // 无折扣
        }
        log.info("User discount rate: {}", discountRate);

        // 6. 计算费用
        BigDecimal hourlyRate = scooter.getPrice();
        BigDecimal baseCost = hourlyRate.multiply(BigDecimal.valueOf(durationHours));
        BigDecimal discountAmount = baseCost.multiply(BigDecimal.ONE.subtract(discountRate));
        BigDecimal finalCost = baseCost.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        log.info("Base cost: {} = {} × {}", baseCost, hourlyRate, durationHours);
        log.info("Discount amount: {}", discountAmount);
        log.info("Final cost: {}", finalCost);

        // 7. 创建订单
        Order order = new Order();
        order.setUserId(request.getUser_id());
        order.setScooterId(request.getScooter_id());
        order.setStartTime(request.getStart_time());
        order.setEndTime(request.getEnd_time());
        order.setDuration(durationHours);
        order.setCost(finalCost);
        order.setStatus(OrderStatus.PENDING);
        order.setExtendedDuration(0.0f);
        order.setDiscount(discountAmount);
        order.setAddress(request.getPickup_address());
        order.setCreatedAt(LocalDateTime.now());

        // 8. 保存订单
        orderMapper.insertOrder(order);
        log.info("Order created successfully: orderId={}", order.getOrderId());

        // 9. 构建响应
        OrderResponse response = new OrderResponse();
        response.setOrder_id(order.getOrderId());
        response.setUser_id(order.getUserId());
        response.setScooter_id(order.getScooterId());
        response.setStart_time(order.getStartTime());
        response.setEnd_time(order.getEndTime());
        response.setCost(order.getCost());
        response.setDiscount_amount(order.getDiscount());
        response.setPickup_address(order.getAddress());
        response.setStatus(order.getStatus().getValue());

        return Optional.of(response);
    }

    /**
     * 获取订单详情
     * 包含订单基本信息和关联的滑板车信息
     *
     * @param orderId 订单ID
     * @return 订单详情，包含滑板车信息
     */
    @Override
    public Optional<OrderDetailResponse> getOrderDetail(Integer orderId) {
        // 查询订单详情（包含滑板车信息）
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);

        // 如果订单不存在，返回空
        if (detailMap == null || detailMap.isEmpty()) {
            return Optional.empty();
        }

        // 使用Java 8 Stream API和函数式编程构建响应对象
        OrderDetailResponse response = new OrderDetailResponse();

        // 设置订单基本信息
        response.setOrder_id((Integer) detailMap.get("order_id"));
        response.setUser_id((Integer) detailMap.get("user_id"));
        response.setScooter_id((Integer) detailMap.get("scooter_id"));
        response.setStart_time((LocalDateTime) detailMap.get("start_time"));
        response.setEnd_time((LocalDateTime) detailMap.get("end_time"));
        response.setExtended_duration((Float) detailMap.get("extended_duration"));
        response.setCost((BigDecimal) detailMap.get("cost"));
        response.setDiscount_amount((BigDecimal) detailMap.get("discount"));
        response.setStatus((String) detailMap.get("status"));
        response.setPickup_address((String) detailMap.get("address"));

        // 构建滑板车信息对象
        OrderDetailResponse.ScooterInfoDto scooterInfo = new OrderDetailResponse.ScooterInfoDto();
        scooterInfo.setLatitude((BigDecimal) detailMap.get("location_lat"));
        scooterInfo.setLongitude((BigDecimal) detailMap.get("location_lng"));
        scooterInfo.setBattery_level((Integer) detailMap.get("battery_level"));
        scooterInfo.setPrice((BigDecimal) detailMap.get("price"));

        // 设置滑板车信息
        response.setScooter_info(scooterInfo);

        return Optional.of(response);
    }

    /**
     * 支付订单
     * 将订单状态从pending更新为paid
     *
     * @param orderId 订单ID
     * @return 支付结果
     */
    @Override
    @Transactional
    public Optional<PayOrderResponse> payOrder(Integer orderId) {
        log.info("Starting process order payment request: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);

        // 2. 验证订单存在
        if (order == null) {
            log.warn("Payment failed: Order {} does not exist", orderId);
            throw OrderException.notFound(orderId);
        }

        // 3. 验证订单状态
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Payment failed: Order {} current status is {}, payment not allowed", orderId, order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.PAID.getValue());
        }

        try {
            // 4. 更新订单状态
            String newStatus;
            if (order.getNewEndTime() != null) {
                // 如果是延长订单，恢复到之前的状态
                newStatus = order.getPreviousStatus().getValue();
                log.info("Extended order {} will be restored to previous status: {}", orderId, newStatus);
            } else {
                // 如果是新订单，设置为已支付状态
                newStatus = OrderStatus.PAID.getValue();
                log.info("New order {} will be set to paid status", orderId);
            }

            // 5. 更新订单状态
            log.info("Updating order {} status to {}", orderId, newStatus);
            int updated = orderMapper.updateOrderStatus(orderId, newStatus);

            // 6. 如果更新成功，返回支付结果
            if (updated > 0) {
                PayOrderResponse response = new PayOrderResponse();
                response.setOrder_id(orderId);
                response.setStatus(newStatus);
                log.info("Order {} payment successful", orderId);

                // 7. 发送订单确认邮件
                try {
                    // 查询用户信息获取邮箱
                    User user = userMapper.findById(order.getUserId().longValue());
                    if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
                        // 异步发送邮件，避免阻塞主流程
                        new Thread(() -> {
                            emailService.sendOrderConfirmationEmail(order, user.getEmail());
                        }).start();
                    } else {
                        log.warn("Unable to send order confirmation email: User {} does not have a valid email address",
                                order.getUserId());
                    }
                } catch (Exception e) {
                    // 邮件发送失败不影响主流程
                    log.error("Error sending order confirmation email: {}", e.getMessage(), e);
                }

                return Optional.of(response);
            } else {
                log.error("Order {} payment failed: Update status failed", orderId);
                throw new OrderException("Payment failed: Update status failed");
            }
        } catch (Exception e) {
            log.error("Order {} payment failed: {}", orderId, e.getMessage(), e);
            throw new OrderException("Payment failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<ChangeOrderStatusResponse> activateOrder(Integer orderId) {
        log.info("Starting process order activation request: orderId={}", orderId);

        // 1. 查询订单详情
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("Activation failed: Order {} does not exist", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        String currentStatus = (String) detailMap.get("status");
        if (!OrderStatus.PAID.getValue().equals(currentStatus)) {
            log.warn("Activation failed: Order {} current status is {}, activation not allowed", orderId,
                    currentStatus);
            throw OrderException.invalidStatus(orderId, currentStatus, OrderStatus.ACTIVE.getValue());
        }

        // 3. 验证订单时间
        LocalDateTime startTime = (LocalDateTime) detailMap.get("start_time");
        if (startTime.isAfter(LocalDateTime.now())) {
            log.warn("Activation failed: Order {} start time {} has not arrived yet", orderId, startTime);
            throw new OrderException("Order start time has not arrived, cannot activate");
        }

        try {
            // 4. 更新订单状态为active
            log.info("Updating order {} status to active", orderId);
            int updated = orderMapper.updateOrderStatus(orderId, OrderStatus.ACTIVE.getValue());

            // 5. 如果更新成功，返回完整的订单信息
            if (updated > 0) {
                ChangeOrderStatusResponse response = new ChangeOrderStatusResponse();

                // 设置订单基本信息
                response.setOrder_id((Integer) detailMap.get("order_id"));
                response.setUser_id((Integer) detailMap.get("user_id"));
                response.setScooter_id((Integer) detailMap.get("scooter_id"));
                response.setStart_time(startTime);
                response.setEnd_time((LocalDateTime) detailMap.get("end_time"));
                response.setCost((BigDecimal) detailMap.get("cost"));
                response.setStatus(OrderStatus.ACTIVE.getValue());
                response.setPickup_address((String) detailMap.get("address"));

                // 设置滑板车信息
                OrderDetailResponse.ScooterInfoDto scooterInfo = new OrderDetailResponse.ScooterInfoDto();
                scooterInfo.setLatitude((BigDecimal) detailMap.get("location_lat"));
                scooterInfo.setLongitude((BigDecimal) detailMap.get("location_lng"));
                scooterInfo.setBattery_level((Integer) detailMap.get("battery_level"));
                scooterInfo.setPrice((BigDecimal) detailMap.get("price"));
                response.setScooter_info(scooterInfo);

                log.info("Order {} activation successful", orderId);
                return Optional.of(response);
            } else {
                log.error("Order {} activation failed: Update status failed", orderId);
                throw new OrderException("Activation failed: Update status failed");
            }
        } catch (Exception e) {
            log.error("Order {} activation failed: {}", orderId, e.getMessage(), e);
            throw new OrderException("Activation failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<ChangeOrderStatusResponse> completeOrder(Integer orderId) {
        log.info("Starting process order completion request: orderId={}", orderId);

        // 1. 查询订单详情
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("Completion failed: Order {} does not exist", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        String currentStatus = (String) detailMap.get("status");
        if (!OrderStatus.ACTIVE.getValue().equals(currentStatus)) {
            log.warn("Completion failed: Order {} current status is {}, completion not allowed", orderId,
                    currentStatus);
            throw OrderException.invalidStatus(orderId, currentStatus, OrderStatus.COMPLETED.getValue());
        }

        try {
            // 3. 更新订单状态为completed
            log.info("Updating order {} status to completed", orderId);
            int updated = orderMapper.updateOrderStatus(orderId, OrderStatus.COMPLETED.getValue());

            // 4. 如果更新成功，返回完整的订单信息
            if (updated > 0) {
                ChangeOrderStatusResponse response = new ChangeOrderStatusResponse();

                // 设置订单基本信息
                response.setOrder_id((Integer) detailMap.get("order_id"));
                response.setUser_id((Integer) detailMap.get("user_id"));
                response.setScooter_id((Integer) detailMap.get("scooter_id"));
                response.setStart_time((LocalDateTime) detailMap.get("start_time"));
                response.setEnd_time((LocalDateTime) detailMap.get("end_time"));
                response.setCost((BigDecimal) detailMap.get("cost"));
                response.setStatus(OrderStatus.COMPLETED.getValue());
                response.setPickup_address((String) detailMap.get("address"));

                // 设置滑板车信息
                OrderDetailResponse.ScooterInfoDto scooterInfo = new OrderDetailResponse.ScooterInfoDto();
                scooterInfo.setLatitude((BigDecimal) detailMap.get("location_lat"));
                scooterInfo.setLongitude((BigDecimal) detailMap.get("location_lng"));
                scooterInfo.setBattery_level((Integer) detailMap.get("battery_level"));
                scooterInfo.setPrice((BigDecimal) detailMap.get("price"));
                response.setScooter_info(scooterInfo);

                log.info("Order {} completion successful", orderId);
                return Optional.of(response);
            } else {
                log.error("Order {} completion failed: Update status failed", orderId);
                throw new OrderException("Completion failed: Update status failed");
            }
        } catch (Exception e) {
            log.error("Order {} completion failed: {}", orderId, e.getMessage(), e);
            throw new OrderException("Completion failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer orderId) {
        log.info("Starting process order deletion request: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            log.warn("Deletion failed: Order {} does not exist", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Deletion failed: Order {} current status is {}, deletion not allowed", orderId,
                    order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.PENDING.getValue());
        }

        try {
            // 3. 删除订单
            log.info("Deleting order {}", orderId);
            int deleted = orderMapper.deleteOrder(orderId, OrderStatus.PENDING.getValue());

            if (deleted > 0) {
                log.info("Order {} deletion successful", orderId);
                return true;
            } else {
                log.error("Order {} deletion failed", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("Order {} deletion failed: {}", orderId, e.getMessage(), e);
            throw new OrderException("Deletion failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean softDeleteOrder(Integer orderId) {
        log.info("Starting process order soft deletion request: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            log.warn("Soft deletion failed: Order {} does not exist", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        if (order.getStatus() != OrderStatus.COMPLETED) {
            log.warn("Soft deletion failed: Order {} current status is {}, soft deletion not allowed", orderId,
                    order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.COMPLETED.getValue());
        }

        try {
            // 3. 更新订单的删除状态
            log.info("Updating order {} deletion status to deleted", orderId);
            int updated = orderMapper.updateOrderDeletedStatus(orderId, true, OrderStatus.COMPLETED.getValue());

            if (updated > 0) {
                log.info("Order {} soft deletion successful", orderId);
                return true;
            } else {
                log.error("Order {} soft deletion failed", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("Order {} soft deletion failed: {}", orderId, e.getMessage(), e);
            throw new OrderException("Soft deletion failed: " + e.getMessage(), e);
        }
    }

    /**
     * 处理超时的pending订单
     * 删除创建时间超过15分钟的pending订单
     */
    @Transactional
    public void handleTimeoutPendingOrders() {
        log.info("Starting process timeout pending orders");
        try {
            // 查询超时的pending订单
            List<Order> timeoutOrders = orderMapper.findTimeoutPendingOrders(15);

            if (timeoutOrders.isEmpty()) {
                log.info("No timeout pending orders to process");
                return;
            }

            // 处理超时的订单
            for (Order order : timeoutOrders) {
                log.info("Processing timeout pending order: orderId={}", order.getOrderId());

                if (order.getNewEndTime() != null) {
                    // 如果是延长订单（new_end_time不为空）
                    // 1. 将new_end_time设为null
                    // 2. 将status恢复为previous_status
                    // 3. 将extended_duration设为0
                    // 4. 将cost减去extended_cost
                    // 5. 将extended_cost设为null
                    log.info("Resetting extended order: orderId={}, previousStatus={}",
                            order.getOrderId(), order.getPreviousStatus());

                    // 计算新的cost
                    BigDecimal newCost = order.getCost().subtract(order.getExtendedCost());

                    // 更新订单
                    orderMapper.resetExtendedOrderWithCost(
                            order.getOrderId(),
                            order.getPreviousStatus().getValue(),
                            newCost);
                } else {
                    // 如果是普通订单（new_end_time为空），直接删除
                    log.info("Deleting timeout pending order: orderId={}", order.getOrderId());
                    orderMapper.deleteOrder(order.getOrderId(), OrderStatus.PENDING.getValue());
                }
            }

            log.info("Successfully processed {} timeout pending orders", timeoutOrders.size());
        } catch (Exception e) {
            log.error("Failed to process timeout pending orders: {}", e.getMessage(), e);
            throw new OrderException("Failed to process timeout pending orders: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<OrderResponse> extendOrder(ExtendOrderRequest request) {
        try {
            // 1. 获取订单信息
            Order order = orderMapper.findById(request.getOrder_id());
            if (order == null) {
                throw new OrderException("订单不存在");
            }

            // 2. 验证订单状态
            if (order.getStatus() != OrderStatus.ACTIVE && order.getStatus() != OrderStatus.PAID) {
                throw new OrderException("只有进行中或已支付的订单可以延长");
            }

            // 3. 验证新的结束时间
            if (!request.getNew_end_time().isAfter(order.getEndTime())) {
                throw new OrderException("新的结束时间必须晚于当前结束时间");
            }

            // 4. 检查时间段是否与其他订单重叠
            List<Order> overlappingOrders = orderMapper.findOverlappingOrders(
                    order.getScooterId(),
                    order.getStartTime(),
                    request.getNew_end_time());
            overlappingOrders.removeIf(o -> o.getOrderId().equals(order.getOrderId()));
            if (!overlappingOrders.isEmpty()) {
                throw new OrderException("该时间段内滑板车已被预订");
            }

            // 5. 计算延长时间（小时）
            Duration duration = Duration.between(order.getEndTime(), request.getNew_end_time());
            float newExtendedDuration = duration.toMinutes() / 60.0f;
            float totalExtendedDuration = order.getExtendedDuration() + newExtendedDuration;

            // 6. 计算延长费用
            BigDecimal currentPrice = orderMapper.getScooterPrice(order.getScooterId());
            BigDecimal extendedPrice = currentPrice.add(new BigDecimal("2.00")); // 延长部分每小时贵2元
            BigDecimal extendedCost = extendedPrice.multiply(BigDecimal.valueOf(newExtendedDuration))
                    .setScale(2, RoundingMode.HALF_UP);

            // 7. 更新订单
            int result = orderMapper.extendOrder(
                    order.getOrderId(),
                    request.getNew_end_time(),
                    totalExtendedDuration,
                    extendedCost,
                    order.getStatus().getValue());

            if (result <= 0) {
                throw new OrderException("延长订单失败");
            }

            // 8. 获取更新后的订单信息
            Order updatedOrder = orderMapper.findById(order.getOrderId());

            // 9. 构建响应
            OrderResponse response = new OrderResponse();
            response.setOrder_id(updatedOrder.getOrderId());
            response.setUser_id(updatedOrder.getUserId());
            response.setScooter_id(updatedOrder.getScooterId());
            response.setStart_time(updatedOrder.getStartTime());
            response.setEnd_time(updatedOrder.getNewEndTime());
            response.setCost(updatedOrder.getCost());
            response.setStatus(updatedOrder.getStatus().getValue());
            response.setPickup_address(updatedOrder.getAddress());

            return Optional.of(response);
        } catch (OrderException e) {
            log.error("延长订单失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("延长订单失败", e);
            throw new OrderException("延长订单失败: " + e.getMessage());
        }
    }

    @Override
    public Optional<AvailableTimeSlotsResponse> getAvailableTimeSlots(Integer orderId) {
        // 1. 查询当前订单
        final Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }

        // 2. 查询下一个订单的开始时间
        LocalDateTime nextStartTime = orderMapper.findNextOrderStartTime(
                order.getScooterId(),
                order.getEndTime());

        // 3. 构建响应
        AvailableTimeSlotsResponse response = new AvailableTimeSlotsResponse();
        response.setCurrent_end_time(order.getEndTime());
        response.setNext_start_time(nextStartTime);

        // 4. 计算最大可延长时间
        if (nextStartTime != null) {
            Duration duration = Duration.between(order.getEndTime(), nextStartTime);
            float maxExtendedHours = duration.toMinutes() / 60.0f;
            response.setMax_extended_hours(maxExtendedHours);
        } else {
            // 如果没有下一个订单，可以延长到任意时间
            response.setMax_extended_hours(Float.MAX_VALUE);
        }

        return Optional.of(response);
    }

    /**
     * 自动完成超时的订单
     * 每分钟执行一次，检查并完成已超过结束时间的active订单
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    @Transactional
    public void autoCompleteTimeoutOrders() {
        log.info("Starting auto complete timeout orders");
        try {
            // 查询所有超时的active订单
            List<Order> timeoutOrders = orderMapper.findTimeoutActiveOrders();

            if (timeoutOrders.isEmpty()) {
                log.info("No timeout active orders to process");
                return;
            }

            // 处理超时的订单
            for (Order order : timeoutOrders) {
                log.info("Processing timeout active order: orderId={}", order.getOrderId());

                // 更新订单状态为completed
                int updated = orderMapper.updateOrderStatus(order.getOrderId(), OrderStatus.COMPLETED.getValue());

                if (updated > 0) {
                    log.info("Order {} auto completed successfully", order.getOrderId());
                } else {
                    log.error("Order {} auto completion failed", order.getOrderId());
                }
            }

            log.info("Successfully processed {} timeout active orders", timeoutOrders.size());
        } catch (Exception e) {
            log.error("Failed to process timeout active orders: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<OrderResponse> getUserOrders(Integer userId) {
        log.info("Getting orders for user: {}", userId);
        try {
            // 查询用户的所有订单
            List<Order> orders = orderMapper.findOrdersByUserId(userId);

            // 转换为OrderResponse列表
            return orders.stream()
                    .map(order -> {
                        OrderResponse response = new OrderResponse();
                        response.setOrder_id(order.getOrderId());
                        response.setUser_id(order.getUserId());
                        response.setScooter_id(order.getScooterId());
                        response.setStart_time(order.getStartTime());
                        response.setEnd_time(order.getEndTime());
                        response.setNew_end_time(order.getNewEndTime());
                        response.setExtended_duration(order.getExtendedDuration());
                        response.setExtended_cost(order.getExtendedCost());
                        response.setCost(order.getCost());
                        response.setDiscount_amount(order.getDiscount());
                        response.setPickup_address(order.getAddress());
                        response.setStatus(order.getStatus().getValue());
                        return response;
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get orders for user {}: {}", userId, e.getMessage(), e);
            throw new OrderException("Failed to get user orders: " + e.getMessage());
        }
    }
}