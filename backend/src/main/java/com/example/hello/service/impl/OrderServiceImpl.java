package com.example.hello.service.impl;

import com.example.hello.common.OrderStatus;
import com.example.hello.dto.request.CreateOrderRequest;
import com.example.hello.dto.response.OrderDetailResponse;
import com.example.hello.dto.response.OrderResponse;
import com.example.hello.dto.response.PayOrderResponse;
import com.example.hello.dto.response.ChangeOrderStatusResponse;
import com.example.hello.dto.request.ExtendOrderRequest;
import com.example.hello.dto.response.AvailableTimeSlotsResponse;
import com.example.hello.dto.response.AvailableCouponsResponse;
import com.example.hello.dto.request.CouponRequest;
import com.example.hello.entity.Order;
import com.example.hello.entity.Scooter;
import com.example.hello.entity.Coupon;
import com.example.hello.exception.OrderException;
import com.example.hello.mapper.OrderMapper;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.mapper.CouponMapper;
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
import java.util.stream.Collectors;

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
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CouponMapper couponMapper;

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
        float durationHours = (float) Math.ceil(duration.toMinutes() / 60.0);
        log.info("Rental duration: {} hours", durationHours);

        // 创建订单时不计算价格
        BigDecimal cost = BigDecimal.ZERO; // 设置为0，不再使用null

        // 创建订单
        Order order = new Order();
        order.setUserId(request.getUser_id());
        order.setScooterId(request.getScooter_id());
        order.setStartTime(request.getStart_time());
        order.setEndTime(request.getEnd_time());
        order.setDuration(durationHours);
        order.setCost(cost);
        order.setStatus(OrderStatus.PENDING);
        order.setExtendedDuration(0.0f);
        order.setAddress(request.getPickup_address());
        order.setCreatedAt(LocalDateTime.now());

        // 保存订单
        orderMapper.insertOrder(order);
        log.info("Order created successfully: orderId={}", order.getOrderId());

        // 构建响应，包含所有订单字段
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
        response.setPickup_address(order.getAddress());
        response.setStatus(order.getStatus().getValue());
        response.setCreated_at(order.getCreatedAt());

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
        log.info("Getting order detail: orderId={}", orderId);

        // 查询订单详情（包含滑板车信息）
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);

        // 如果订单不存在，返回空
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("Order {} does not exist", orderId);
            return Optional.empty();
        }

        // 使用Java 8 Stream API和函数式编程构建响应对象
        OrderDetailResponse response = new OrderDetailResponse();

        // 获取基础数据
        LocalDateTime startTime = (LocalDateTime) detailMap.get("start_time");
        LocalDateTime endTime = (LocalDateTime) detailMap.get("end_time");
        Float duration = (Float) detailMap.get("duration");
        BigDecimal hourlyPrice = (BigDecimal) detailMap.get("price");
        BigDecimal baseCost = (BigDecimal) detailMap.get("cost");

        // 计算租赁天数
        long days = 0;
        if (startTime != null && endTime != null) {
            Duration between = java.time.Duration.between(startTime, endTime);
            days = (long) Math.ceil(between.toHours() / 24.0);
        }

        // 计算价格（单价*小时数）
        BigDecimal calculatedCost = BigDecimal.ZERO;
        if (hourlyPrice != null && duration != null) {
            calculatedCost = hourlyPrice.multiply(BigDecimal.valueOf(duration));
        } else {
            // 如果无法计算，则使用数据库中的cost值
            calculatedCost = baseCost;
        }

        // 设置订单基本信息
        response.setOrder_id((Integer) detailMap.get("order_id"));
        response.setUser_id((Integer) detailMap.get("user_id"));
        response.setScooter_id((Integer) detailMap.get("scooter_id"));
        response.setStart_time(startTime);
        response.setEnd_time(endTime);
        response.setRental_days((int) days);
        response.setDuration(duration);
        response.setExtended_duration((Float) detailMap.get("extended_duration"));
        response.setExtended_cost((BigDecimal) detailMap.get("extended_cost"));
        response.setCost(calculatedCost); // 直接设置计算后的价格为cost
        response.setStatus((String) detailMap.get("status"));
        response.setPickup_address((String) detailMap.get("address"));
        response.setCreated_at((LocalDateTime) detailMap.get("create_at"));
        response.setNew_end_time((LocalDateTime) detailMap.get("new_end_time"));
        response.setPrevious_status((String) detailMap.get("previous_status"));
        response.setIs_deleted((Boolean) detailMap.get("is_deleted"));

        // 构建滑板车信息对象
        OrderDetailResponse.ScooterInfoDto scooterInfo = new OrderDetailResponse.ScooterInfoDto();
        scooterInfo.setLatitude((BigDecimal) detailMap.get("location_lat"));
        scooterInfo.setLongitude((BigDecimal) detailMap.get("location_lng"));
        scooterInfo.setBattery_level((Integer) detailMap.get("battery_level"));
        scooterInfo.setPrice(hourlyPrice);

        // 设置滑板车型号和编号
        scooterInfo.setStyle("Standard"); // 默认型号，可以从数据库读取
        scooterInfo.setNumber("S" + detailMap.get("scooter_id")); // 生成编号

        // 设置滑板车信息
        response.setScooter_info(scooterInfo);

        log.info("Order detail retrieved successfully: orderId={}", orderId);
        return Optional.of(response);
    }

    /**
     * 支付订单
     * 将订单状态从pending更新为paid
     *
     * @param orderId       订单ID
     * @param couponRequest 优惠券请求（可选）
     * @return 支付结果
     */
    @Override
    @Transactional
    public Optional<PayOrderResponse> payOrder(Integer orderId, CouponRequest couponRequest) {
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
            // 4. 计算实际订单价格（租赁时长 * 每小时价格）
            BigDecimal hourlyPrice = orderMapper.getScooterPrice(order.getScooterId());
            if (hourlyPrice == null) {
                log.warn("Payment failed: Scooter {} has no price", order.getScooterId());
                throw new OrderException("滑板车价格不存在");
            }

            // 获取实际租赁时长（小时）
            float durationHours = order.getDuration();

            // 计算基础价格
            BigDecimal basePrice = hourlyPrice.multiply(BigDecimal.valueOf(durationHours))
                    .setScale(2, RoundingMode.HALF_UP);

            log.info("Calculated order base price: orderId={}, hourlyPrice={}, duration={}, basePrice={}",
                    orderId, hourlyPrice, durationHours, basePrice);

            // 更新订单的基础价格
            orderMapper.updateOrderCostAndDiscount(orderId, basePrice, order.getDiscount());
            order.setCost(basePrice);

            // 5. 处理优惠券（如果有）
            BigDecimal discountAmount = BigDecimal.ZERO;

            if (couponRequest != null && couponRequest.getCouponId() != null) {
                log.info("开始处理优惠券: couponId={}, orderId={}, 订单基础金额={}",
                        couponRequest.getCouponId(), orderId, order.getCost());

                // 5.1 查询优惠券
                Coupon coupon = couponMapper.findById(couponRequest.getCouponId());

                if (coupon == null) {
                    log.warn("支付失败: 优惠券{}不存在", couponRequest.getCouponId());
                    throw new OrderException("优惠券不存在");
                }

                // 5.2 验证优惠券是否可用
                if (!coupon.getIsActive()) {
                    log.warn("支付失败: 优惠券{}已失效", couponRequest.getCouponId());
                    throw new OrderException("优惠券已失效");
                }

                // 5.3 验证优惠券是否在有效期内
                LocalDateTime now = LocalDateTime.now();
                if (coupon.getValidFrom().isAfter(now.toLocalDate())
                        || coupon.getValidTo().isBefore(now.toLocalDate())) {
                    log.warn("支付失败: 优惠券{}不在有效期内 (有效期: {} 至 {})",
                            couponRequest.getCouponId(),
                            coupon.getValidFrom(),
                            coupon.getValidTo());
                    throw new OrderException("优惠券不在有效期内");
                }

                // 5.4 验证是否满足最低消费
                if (coupon.getMinSpend() != null && order.getCost().compareTo(coupon.getMinSpend()) < 0) {
                    log.warn("支付失败: 订单金额{}未达到优惠券最低消费要求{}",
                            order.getCost(), coupon.getMinSpend());
                    throw new OrderException(String.format("订单金额%.2f未达到优惠券最低消费要求%.2f",
                            order.getCost().doubleValue(),
                            coupon.getMinSpend().doubleValue()));
                }

                // 5.5 验证用户是否拥有该优惠券
                boolean userOwnsCoupon = couponMapper.checkUserCoupon(order.getUserId(), coupon.getCouponId());
                if (!userOwnsCoupon) {
                    log.warn("支付失败: 用户{}未拥有优惠券{}", order.getUserId(), coupon.getCouponId());
                    throw new OrderException("您未拥有该优惠券");
                }

                // 5.6 使用优惠券
                int updated = couponMapper.useCoupon(order.getUserId(), coupon.getCouponId(), orderId);
                if (updated <= 0) {
                    log.warn("支付失败: 优惠券{}已被使用或不属于用户{}",
                            coupon.getCouponId(), order.getUserId());
                    throw new OrderException("优惠券已使用或不属于该用户");
                }

                // 5.7 计算折扣金额
                discountAmount = coupon.getCouponAmount();

                // 5.8 更新订单折扣信息
                BigDecimal finalCost = order.getCost().subtract(discountAmount);
                if (finalCost.compareTo(BigDecimal.ZERO) < 0) {
                    log.info("折扣金额{}大于订单金额{}，最终价格将被设置为0",
                            discountAmount, order.getCost());
                    finalCost = BigDecimal.ZERO;
                }

                orderMapper.updateOrderCostAndDiscount(orderId, finalCost, discountAmount);
                order.setCost(finalCost);
                order.setDiscount(discountAmount);

                log.info("优惠券应用成功: orderId={}, 原价={}, 折扣={}, 最终价格={}",
                        orderId, basePrice, discountAmount, finalCost);
            }

            // 6. 更新订单状态
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

            // 7. 更新订单状态
            log.info("Updating order {} status to {}", orderId, newStatus);
            int updated = orderMapper.updateOrderStatus(orderId, newStatus);

            // 8. 如果更新成功，返回支付结果
            if (updated > 0) {
                PayOrderResponse response = new PayOrderResponse();
                response.setOrder_id(orderId);
                response.setUser_id(order.getUserId());
                response.setStatus(newStatus);
                response.setCost(order.getCost());

                // 设置优惠券信息
                if (couponRequest != null && couponRequest.getCouponId() != null) {
                    response.setCoupon_id(couponRequest.getCouponId());
                    response.setCoupon_amount(discountAmount);
                }

                log.info("Order {} payment successful, cost={}, couponId={}, couponAmount={}",
                        orderId, order.getCost(),
                        response.getCoupon_id(), response.getCoupon_amount());

                // 9. 发送订单确认邮件
                try {
                    // 查询用户信息获取邮箱
                    User user = userMapper.findById(order.getUserId().longValue());
                    if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
                        log.info("Sending order confirmation email to user: {}, email: {}",
                                order.getUserId(), user.getEmail());
                        // 立即发送邮件，不使用异步方式
                        emailService.sendOrderConfirmationEmail(order, user.getEmail());
                        log.info("Order confirmation email sent successfully");
                    } else {
                        log.warn("Unable to send order confirmation email: User {} does not have a valid email address",
                                order.getUserId());
                    }
                } catch (Exception e) {
                    // 邮件发送失败不影响主流程，但需要记录错误
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

    /**
     * 获取用户所有未使用的优惠券
     * 
     * @param userId 用户ID
     * @return 优惠券列表
     */
    @Override
    public Optional<AvailableCouponsResponse> getAvailableCoupons(Integer userId) {
        log.info("Getting available coupons for user: userId={}", userId);

        try {
            // 查询用户所有未使用的优惠券
            List<Coupon> allCoupons = couponMapper.findUserCoupons(userId);

            // 筛选未使用的优惠券
            List<Coupon> availableCoupons = allCoupons.stream()
                    .filter(coupon -> !coupon.getIsUsed())
                    .peek(coupon -> {
                        // 验证有效期
                        LocalDateTime now = LocalDateTime.now();
                        boolean isValid = !coupon.getValidFrom().isAfter(now.toLocalDate()) &&
                                !coupon.getValidTo().isBefore(now.toLocalDate());

                        // 设置可用状态
                        coupon.setStatus(isValid ? "able" : "disabled");
                    })
                    .toList();

            log.info("Found {} available coupons for user {}", availableCoupons.size(), userId);

            return Optional.of(AvailableCouponsResponse.of(availableCoupons));
        } catch (Exception e) {
            log.error("Get available coupons failed: {}", e.getMessage(), e);
            throw new OrderException("Get available coupons failed: " + e.getMessage(), e);
        }
    }

    /**
     * 支付订单（不使用优惠券，保持与原有方法兼容）
     *
     * @param orderId 订单ID
     * @return 支付结果
     */
    @Override
    public Optional<PayOrderResponse> payOrder(Integer orderId) {
        return payOrder(orderId, null);
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

            // 5. 更新滑板车状态为使用中
            Integer scooterId = (Integer) detailMap.get("scooter_id");
            log.info("Updating scooter {} status to in_use", scooterId);
            scooterMapper.updateScooterStatus(scooterId, Scooter.Status.in_use.name());

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

            // 4. 更新滑板车状态为空闲
            Integer scooterId = (Integer) detailMap.get("scooter_id");
            log.info("Updating scooter {} status to free", scooterId);
            scooterMapper.updateScooterStatus(scooterId, Scooter.Status.free.name());

            // 5. 更新用户使用时间和总金额
            Integer userId = (Integer) detailMap.get("user_id");
            Float duration = (Float) detailMap.get("duration");
            Float extendedDuration = (Float) detailMap.get("extended_duration");
            BigDecimal cost = (BigDecimal) detailMap.get("cost");

            // 计算总使用时间
            Float totalDuration = duration + (extendedDuration != null ? extendedDuration : 0.0f);

            // 更新用户使用统计
            userMapper.updateUserUsageStats(userId, totalDuration, cost);
            log.info("Updated user {} usage stats: added_usage_hours={}, added_spent={}",
                    userId, totalDuration, cost);

            // 6. 如果更新成功，返回完整的订单信息
            if (updated > 0) {
                ChangeOrderStatusResponse response = new ChangeOrderStatusResponse();

                // 设置订单基本信息
                response.setOrder_id((Integer) detailMap.get("order_id"));
                response.setUser_id(userId);
                response.setScooter_id(scooterId);
                response.setStart_time((LocalDateTime) detailMap.get("start_time"));
                response.setEnd_time((LocalDateTime) detailMap.get("end_time"));
                response.setCost(cost);
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
            response.setExtended_cost(extendedCost);
            response.setExtended_duration(newExtendedDuration);

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
                        response.setPickup_address(order.getAddress());
                        response.setStatus(order.getStatus().getValue());
                        response.setCreated_at(order.getCreatedAt());
                        return response;
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get orders for user {}: {}", userId, e.getMessage(), e);
            throw new OrderException("Failed to get user orders: " + e.getMessage());
        }
    }

    /**
     * 获取订单原始信息
     * 不同于getOrderDetail，此方法不进行价格计算等额外处理
     * 只返回数据库中的原始信息
     *
     * @param orderId 订单ID
     * @return 订单原始信息
     */
    @Override
    public Optional<OrderDetailResponse> getOrderRawInfo(Integer orderId) {
        log.info("Getting raw order information: orderId={}", orderId);

        // 查询订单详情（包含滑板车信息）
        Map<String, Object> detailMap = orderMapper.getOrderDetail(orderId);

        // 如果订单不存在，返回空
        if (detailMap == null || detailMap.isEmpty()) {
            log.warn("Order {} does not exist", orderId);
            return Optional.empty();
        }

        // 构建响应对象
        OrderDetailResponse response = new OrderDetailResponse();

        // 获取基础数据
        LocalDateTime startTime = (LocalDateTime) detailMap.get("start_time");
        LocalDateTime endTime = (LocalDateTime) detailMap.get("end_time");
        BigDecimal hourlyPrice = (BigDecimal) detailMap.get("price");

        // 设置订单基本信息 - 直接使用数据库中的原始值，不进行计算
        response.setOrder_id((Integer) detailMap.get("order_id"));
        response.setUser_id((Integer) detailMap.get("user_id"));
        response.setScooter_id((Integer) detailMap.get("scooter_id"));
        response.setStart_time(startTime);
        response.setEnd_time(endTime);
        response.setDuration((Float) detailMap.get("duration"));
        response.setExtended_duration((Float) detailMap.get("extended_duration"));
        response.setExtended_cost((BigDecimal) detailMap.get("extended_cost"));
        // 直接使用数据库中的cost值，不重新计算
        response.setCost((BigDecimal) detailMap.get("cost"));
        response.setDiscount_amount((BigDecimal) detailMap.get("discount"));
        response.setStatus((String) detailMap.get("status"));
        response.setPickup_address((String) detailMap.get("address"));
        response.setCreated_at((LocalDateTime) detailMap.get("create_at"));
        response.setNew_end_time((LocalDateTime) detailMap.get("new_end_time"));
        response.setPrevious_status((String) detailMap.get("previous_status"));
        response.setIs_deleted((Boolean) detailMap.get("is_deleted"));

        // 构建滑板车信息对象
        OrderDetailResponse.ScooterInfoDto scooterInfo = new OrderDetailResponse.ScooterInfoDto();
        scooterInfo.setLatitude((BigDecimal) detailMap.get("location_lat"));
        scooterInfo.setLongitude((BigDecimal) detailMap.get("location_lng"));
        scooterInfo.setBattery_level((Integer) detailMap.get("battery_level"));
        scooterInfo.setPrice(hourlyPrice);

        // 设置滑板车型号和编号
        scooterInfo.setStyle("Standard"); // 默认型号，可以从数据库读取
        scooterInfo.setNumber("S" + detailMap.get("scooter_id")); // 生成编号

        // 设置滑板车信息
        response.setScooter_info(scooterInfo);

        log.info("Raw order information retrieved successfully: orderId={}", orderId);
        return Optional.of(response);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderMapper.findAllOrders();
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
                    response.setCreated_at(order.getCreatedAt());
                    response.setIs_deleted(order.getIsDeleted());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalRevenue() {
        log.info("Calculating total revenue");
        BigDecimal totalRevenue = orderMapper.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }
        log.info("Total revenue: {}", totalRevenue);
        return totalRevenue;
    }

    @Override
    public BigDecimal getDailyRevenue() {
        return orderMapper.getDailyRevenue();
    }

    @Override
    public BigDecimal getWeeklyRevenue() {
        return orderMapper.getWeeklyRevenue();
    }

    /**
     * 每分钟检查并记录总收入
     */
    @Scheduled(fixedRate = 60000)
    public void checkTotalRevenue() {
        BigDecimal totalRevenue = getTotalRevenue();
        log.info("Current total revenue: {}", totalRevenue);
    }

    /**
     * 每分钟检查并记录当天收入
     */
    @Scheduled(fixedRate = 60000)
    public void checkDailyRevenue() {
        BigDecimal dailyRevenue = getDailyRevenue();
        log.info("Current daily revenue: {}", dailyRevenue);
    }

    /**
     * 每分钟检查并记录最近一周收入
     */
    @Scheduled(fixedRate = 60000)
    public void checkWeeklyRevenue() {
        BigDecimal weeklyRevenue = getWeeklyRevenue();
        log.info("Current weekly revenue: {}", weeklyRevenue);
    }
}