package com.example.hello.service.impl;

import com.example.hello.common.OrderStatus;
import com.example.hello.dto.CreateOrderRequest;
import com.example.hello.dto.OrderDetailResponse;
import com.example.hello.dto.OrderResponse;
import com.example.hello.dto.PayOrderResponse;
import com.example.hello.dto.ChangeOrderStatusResponse;
import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.exception.OrderException;
import com.example.hello.mapper.OrderMapper;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
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
        // 1. 检查滑板车是否存在且可用
        Scooter scooter = scooterMapper.findById(request.getScooter_id());
        if (scooter == null) {
            throw new RuntimeException("滑板车不存在");
        }

        if (!"free".equals(scooter.getStatus())) {
            throw new RuntimeException("滑板车当前不可用，状态为: " + scooter.getStatus());
        }

        // 2. 验证开始时间和结束时间
        if (request.getEnd_time().isBefore(request.getStart_time())) {
            throw new RuntimeException("结束时间不能早于开始时间");
        }

        // 3. 计算使用时长（小时）
        Duration duration = Duration.between(request.getStart_time(), request.getEnd_time());
        float durationHours = duration.toMinutes() / 60.0f;

        // 4. 计算费用
        BigDecimal hourlyRate = scooter.getPrice();
        BigDecimal cost = hourlyRate.multiply(BigDecimal.valueOf(durationHours))
                .setScale(2, RoundingMode.HALF_UP);

        // 5. 创建订单
        Order order = new Order();
        order.setUserId(request.getUser_id());
        order.setScooterId(request.getScooter_id());
        order.setStartTime(request.getStart_time());
        order.setEndTime(request.getEnd_time());
        order.setDuration(durationHours);
        order.setCost(cost);
        order.setStatus(OrderStatus.PENDING);
        order.setExtendedDuration(0.0f);
        // 初始折扣设为0
        order.setDiscount(BigDecimal.ZERO);
        order.setAddress(request.getPickup_address());

        // 6. 保存订单
        orderMapper.insertOrder(order);

        // 7. 更新滑板车状态为已预订
        scooterMapper.updateScooterStatus(request.getScooter_id(), "booked");

        // 8. 构建响应
        OrderResponse response = new OrderResponse();
        response.setOrder_id(order.getOrderId());
        response.setUser_id(order.getUserId());
        response.setScooter_id(order.getScooterId());
        response.setStart_time(order.getStartTime());
        response.setEnd_time(order.getEndTime());
        response.setCost(order.getCost());
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
        response.setCost((BigDecimal) detailMap.get("cost"));
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

            // 5. 更新滑板车状态为in_use
            Integer scooterId = (Integer) detailMap.get("scooter_id");
            scooterMapper.updateScooterStatus(scooterId, "in_use");

            // 6. 如果更新成功，返回完整的订单信息
            if (updated > 0) {
                ChangeOrderStatusResponse response = new ChangeOrderStatusResponse();

                // 设置订单基本信息
                response.setOrder_id((Integer) detailMap.get("order_id"));
                response.setUser_id((Integer) detailMap.get("user_id"));
                response.setScooter_id(scooterId);
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

                // 设置滑板车状态
                response.setScooter_status("in_use");

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

            // 4. 更新滑板车状态为free
            Integer scooterId = (Integer) detailMap.get("scooter_id");
            scooterMapper.updateScooterStatus(scooterId, "free");

            // 5. 如果更新成功，返回完整的订单信息
            if (updated > 0) {
                ChangeOrderStatusResponse response = new ChangeOrderStatusResponse();

                // 设置订单基本信息
                response.setOrder_id((Integer) detailMap.get("order_id"));
                response.setUser_id((Integer) detailMap.get("user_id"));
                response.setScooter_id(scooterId);
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

                // 设置滑板车状态
                response.setScooter_status("free");

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

            // 4. 更新滑板车状态为free
            if (deleted > 0) {
                scooterMapper.updateScooterStatus(order.getScooterId(), "free");
                log.info("订单{}删除成功，滑板车状态已更新为可用", orderId);
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
}