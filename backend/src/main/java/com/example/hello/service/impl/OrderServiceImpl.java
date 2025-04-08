package com.example.hello.service.impl;

import com.example.hello.common.OrderStatus;
import com.example.hello.dto.CreateOrderRequest;
import com.example.hello.dto.OrderDetailResponse;
import com.example.hello.dto.OrderResponse;
import com.example.hello.dto.PayOrderResponse;
import com.example.hello.dto.ChangeOrderStatusResponse;
import com.example.hello.dto.ExtendOrderRequest;
import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.exception.OrderException;
import com.example.hello.mapper.OrderMapper;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.mapper.DiscountMapper;
import com.example.hello.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
        log.info("开始创建订单: userId={}, scooterId={}", request.getUser_id(), request.getScooter_id());
        
        // 1. 检查滑板车是否存在
        Scooter scooter = scooterMapper.findById(request.getScooter_id());
        if (scooter == null) {
            log.error("滑板车不存在: scooterId={}", request.getScooter_id());
            throw new RuntimeException("滑板车不存在");
        }

        // 2. 验证开始时间和结束时间
        if (request.getEnd_time().isBefore(request.getStart_time())) {
            log.error("结束时间不能早于开始时间: startTime={}, endTime={}", request.getStart_time(), request.getEnd_time());
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        // 3. 检查时间段是否与其他订单重叠
        List<Order> overlappingOrders = orderMapper.findOverlappingOrders(
                request.getScooter_id(),
                request.getStart_time(),
                request.getEnd_time());
        if (!overlappingOrders.isEmpty()) {
            log.error("该时间段内滑板车已被预订: scooterId={}, startTime={}, endTime={}", 
                    request.getScooter_id(), request.getStart_time(), request.getEnd_time());
            throw new RuntimeException("该时间段内滑板车已被预订");
        }

        // 4. 计算使用时长（小时）
        Duration duration = Duration.between(request.getStart_time(), request.getEnd_time());
        float durationHours = duration.toMinutes() / 60.0f;
        log.info("租用时长: {} 小时", durationHours);

        // 5. 获取用户折扣率（如果有）
        BigDecimal discountRate = discountMapper.getUserDiscountRate(request.getUser_id());
        if (discountRate == null) {
            discountRate = BigDecimal.ONE; // 无折扣
        }
        log.info("用户折扣率: {}", discountRate);

        // 6. 计算费用
        BigDecimal hourlyRate = scooter.getPrice();
        BigDecimal baseCost = hourlyRate.multiply(BigDecimal.valueOf(durationHours));
        BigDecimal discountAmount = baseCost.multiply(BigDecimal.ONE.subtract(discountRate));
        BigDecimal finalCost = baseCost.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
        
        log.info("基础费用: {} = {} × {}", baseCost, hourlyRate, durationHours);
        log.info("折扣金额: {}", discountAmount);
        log.info("最终费用: {}", finalCost);

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
        log.info("订单创建成功: orderId={}", order.getOrderId());

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
        log.info("开始处理订单支付请求: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);

        // 2. 验证订单存在
        if (order == null) {
            log.warn("支付失败: 订单{}不存在", orderId);
            throw OrderException.notFound(orderId);
        }

        // 3. 验证订单状态
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("支付失败: 订单{}当前状态为{}, 不允许支付", orderId, order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.PAID.getValue());
        }

        try {
            // 4. 更新订单状态为paid
            log.info("更新订单{}状态为paid", orderId);
            int updated = orderMapper.updateOrderStatus(orderId, OrderStatus.PAID.getValue());

            // 5. 如果更新成功，返回支付结果
            if (updated > 0) {
                PayOrderResponse response = new PayOrderResponse();
                response.setOrder_id(orderId);
                response.setStatus(OrderStatus.PAID.getValue());
                log.info("订单{}支付成功", orderId);
                return Optional.of(response);
            } else {
                log.error("订单{}支付失败: 更新状态失败", orderId);
                throw new OrderException("支付失败: 更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("订单{}支付失败: {}", orderId, e.getMessage(), e);
            throw new OrderException("支付失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<ChangeOrderStatusResponse> activateOrder(Integer orderId) {
        log.info("开始处理订单激活请求: orderId={}", orderId);

        // 1. 查询订单详情
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("激活失败: 订单{}不存在", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        String currentStatus = (String) detailMap.get("status");
        if (!OrderStatus.PAID.getValue().equals(currentStatus)) {
            log.warn("激活失败: 订单{}当前状态为{}, 不允许激活", orderId, currentStatus);
            throw OrderException.invalidStatus(orderId, currentStatus, OrderStatus.ACTIVE.getValue());
        }

        // 3. 验证订单时间
        LocalDateTime startTime = (LocalDateTime) detailMap.get("start_time");
        if (startTime.isAfter(LocalDateTime.now())) {
            log.warn("激活失败: 订单{}的开始时间{}还未到达", orderId, startTime);
            throw new OrderException("订单开始时间未到达，无法激活");
        }

        try {
            // 4. 更新订单状态为active
            log.info("更新订单{}状态为active", orderId);
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

                log.info("订单{}激活成功", orderId);
                return Optional.of(response);
            } else {
                log.error("订单{}激活失败: 更新状态失败", orderId);
                throw new OrderException("激活失败: 更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("订单{}激活失败: {}", orderId, e.getMessage(), e);
            throw new OrderException("激活失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<ChangeOrderStatusResponse> completeOrder(Integer orderId) {
        log.info("开始处理订单完成请求: orderId={}", orderId);

        // 1. 查询订单详情
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("完成失败: 订单{}不存在", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        String currentStatus = (String) detailMap.get("status");
        if (!OrderStatus.ACTIVE.getValue().equals(currentStatus)) {
            log.warn("完成失败: 订单{}当前状态为{}, 不允许完成", orderId, currentStatus);
            throw OrderException.invalidStatus(orderId, currentStatus, OrderStatus.COMPLETED.getValue());
        }

        try {
            // 3. 更新订单状态为completed
            log.info("更新订单{}状态为completed", orderId);
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

                log.info("订单{}完成成功", orderId);
                return Optional.of(response);
            } else {
                log.error("订单{}完成失败: 更新状态失败", orderId);
                throw new OrderException("完成失败: 更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("订单{}完成失败: {}", orderId, e.getMessage(), e);
            throw new OrderException("完成失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer orderId) {
        log.info("开始处理订单删除请求: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            log.warn("删除失败: 订单{}不存在", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("删除失败: 订单{}当前状态为{}, 不允许删除", orderId, order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.PENDING.getValue());
        }

        try {
            // 3. 删除订单
            log.info("删除订单{}", orderId);
            int deleted = orderMapper.deleteOrder(orderId, OrderStatus.PENDING.getValue());

            if (deleted > 0) {
                log.info("订单{}删除成功", orderId);
                return true;
            } else {
                log.error("订单{}删除失败", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("订单{}删除失败: {}", orderId, e.getMessage(), e);
            throw new OrderException("删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean softDeleteOrder(Integer orderId) {
        log.info("开始处理订单软删除请求: orderId={}", orderId);

        // 1. 查询订单
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            log.warn("软删除失败: 订单{}不存在", orderId);
            throw OrderException.notFound(orderId);
        }

        // 2. 验证订单状态
        if (order.getStatus() != OrderStatus.COMPLETED) {
            log.warn("软删除失败: 订单{}当前状态为{}, 不允许软删除", orderId, order.getStatus());
            throw OrderException.invalidStatus(orderId, order.getStatus().getValue(), OrderStatus.COMPLETED.getValue());
        }

        try {
            // 3. 更新订单的删除状态
            log.info("更新订单{}的删除状态为已删除", orderId);
            int updated = orderMapper.updateOrderDeletedStatus(orderId, true, OrderStatus.COMPLETED.getValue());

            if (updated > 0) {
                log.info("订单{}软删除成功", orderId);
                return true;
            } else {
                log.error("订单{}软删除失败", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("订单{}软删除失败: {}", orderId, e.getMessage(), e);
            throw new OrderException("软删除失败: " + e.getMessage(), e);
        }
    }

    /**
     * 处理超时的pending订单
     * 删除创建时间超过15分钟的pending订单
     */
    @Transactional
    public void handleTimeoutPendingOrders() {
        log.info("开始处理超时的pending订单");
        try {
            // 查询超时的pending订单
            List<Order> timeoutOrders = orderMapper.findTimeoutPendingOrders(15);

            if (timeoutOrders.isEmpty()) {
                log.info("没有需要处理的超时pending订单");
                return;
            }

            // 删除超时的订单
            for (Order order : timeoutOrders) {
                log.info("删除超时pending订单: orderId={}", order.getOrderId());
                orderMapper.deleteOrder(order.getOrderId(), OrderStatus.PENDING.getValue());
            }

            log.info("成功处理{}个超时pending订单", timeoutOrders.size());
        } catch (Exception e) {
            log.error("处理超时pending订单失败: {}", e.getMessage(), e);
            throw new OrderException("处理超时pending订单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<OrderResponse> extendOrder(ExtendOrderRequest request) {
        // 1. 检查订单是否存在且状态为active或paid
        final Order order = orderMapper.findById(request.getOrder_id());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getStatus() != OrderStatus.ACTIVE && order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("只有进行中和已支付的订单可以延长");
        }

        // 2. 检查新的结束时间是否晚于当前结束时间
        if (!request.getNew_end_time().isAfter(order.getEndTime())) {
            throw new RuntimeException("新的结束时间必须晚于当前结束时间");
        }

        // 3. 检查时间段是否与其他订单重叠（排除completed状态）
        List<Order> overlappingOrders = orderMapper.findOverlappingOrders(
                order.getScooterId(),
                order.getStartTime(),
                request.getNew_end_time());

        // 过滤掉当前订单
        overlappingOrders.removeIf(o -> o.getOrderId().equals(order.getOrderId()));

        if (!overlappingOrders.isEmpty()) {
            throw new RuntimeException("该时间段内滑板车已被预订");
        }

        // 4. 计算延长时间（小时）
        Duration duration = Duration.between(order.getEndTime(), request.getNew_end_time());
        float newExtendedDuration = duration.toMinutes() / 60.0f;
        // 总延长时间 = 原有延长时间 + 新的延长时间
        float totalExtendedDuration = order.getExtendedDuration() + newExtendedDuration;

        // 5. 计算新的费用
        // 获取滑板车当前价格
        BigDecimal currentPrice = orderMapper.getScooterPrice(order.getScooterId());
        // 延长部分的价格比原来贵2元
        BigDecimal extendedPrice = currentPrice.add(new BigDecimal("2.00"));
        // 计算延长部分的费用
        BigDecimal extendedCost = extendedPrice.multiply(BigDecimal.valueOf(newExtendedDuration))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        // 总费用 = 原费用 + 延长费用
        BigDecimal newCost = order.getCost().add(extendedCost);

        // 6. 更新订单
        orderMapper.extendOrder(
                order.getOrderId(),
                request.getNew_end_time(),
                totalExtendedDuration,
                newCost);

        // 7. 重新查询订单以获取最新数据
        final Order updatedOrder = orderMapper.findById(order.getOrderId());

        // 8. 构建响应
        OrderResponse response = new OrderResponse();
        response.setOrder_id(updatedOrder.getOrderId());
        response.setUser_id(updatedOrder.getUserId());
        response.setScooter_id(updatedOrder.getScooterId());
        response.setStart_time(updatedOrder.getStartTime());
        response.setEnd_time(updatedOrder.getEndTime()); // 使用更新后的结束时间
        response.setCost(newCost);
        response.setPickup_address(updatedOrder.getAddress());
        response.setStatus(updatedOrder.getStatus().getValue());

        return Optional.of(response);
    }
}