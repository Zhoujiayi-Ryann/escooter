package com.example.hello.service;

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
import java.math.BigDecimal;
import java.util.List;
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
     * 管理员为未注册用户创建订单
     * 创建后直接设置为已支付状态，无需用户付款
     *
     * @param request 创建订单请求
     * @param adminId 管理员ID
     * @param cost 管理员设置的价格
     * @return 创建的订单信息
     */
    Optional<OrderResponse> createOrderForTempUser(CreateOrderRequest request, Integer adminId, BigDecimal cost);

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
     * @param orderId       订单ID
     * @param couponRequest 优惠券请求（可选）
     * @return 支付结果
     */
    Optional<PayOrderResponse> payOrder(Integer orderId, CouponRequest couponRequest);

    /**
     * 支付订单（不使用优惠券）
     * 将订单状态从pending更新为paid
     *
     * @param orderId 订单ID
     * @return 支付结果
     */
    Optional<PayOrderResponse> payOrder(Integer orderId);

    /**
     * 激活订单
     * 将订单状态从paid更新为active
     *
     * @param orderId 订单ID
     * @return 激活结果
     */
    Optional<ChangeOrderStatusResponse> activateOrder(Integer orderId);

    /**
     * 完成订单
     * 将订单状态从active更新为completed
     *
     * @param orderId 订单ID
     * @return 完成结果
     */
    Optional<ChangeOrderStatusResponse> completeOrder(Integer orderId);

    /**
     * 删除未支付订单
     * 只能删除状态为pending的订单
     *
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    boolean deleteOrder(Integer orderId);

    /**
     * 软删除已完成的订单
     * 只能删除状态为completed的订单
     *
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    boolean softDeleteOrder(Integer orderId);

    /**
     * 延长订单时间
     * 
     * @param request 延长订单请求
     * @return 更新后的订单信息
     */
    Optional<OrderResponse> extendOrder(ExtendOrderRequest request);

    /**
     * 获取订单可延长的时间段
     * 
     * @param orderId 订单ID
     * @return 可用时间段信息
     */
    Optional<AvailableTimeSlotsResponse> getAvailableTimeSlots(Integer orderId);

    /**
     * 获取用户的所有订单
     * 按创建时间倒序排序
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<OrderResponse> getUserOrders(Integer userId);

    /**
     * 获取用户所有未使用的优惠券
     * 
     * @param userId 用户ID
     * @return 优惠券列表
     */
    Optional<AvailableCouponsResponse> getAvailableCoupons(Integer userId);

    /**
     * 获取订单原始信息
     * 不同于getOrderDetail，此方法不进行价格计算等额外处理
     * 只返回数据库中的原始信息
     *
     * @param orderId 订单ID
     * @return 订单原始信息
     */
    Optional<OrderDetailResponse> getOrderRawInfo(Integer orderId);

    List<OrderResponse> getAllOrders();

    /**
     * 获取系统总收入
     * 计算所有非pending状态订单的cost总和
     *
     * @return 系统总收入
     */
    BigDecimal getTotalRevenue();

    /**
     * 获取当天的收入（不包括pending的订单）
     * 
     * @return 当天的收入总额
     */
    BigDecimal getDailyRevenue();

    /**
     * 获取指定时间段的收入（不包括pending的订单）
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 指定时间段的收入总额
     */
    BigDecimal getRevenueByDateRange(String startDate, String endDate);

    /**
     * 获取指定时间段的收入统计
     * 包括：
     * 1. 每天的总收入
     * 2. 每天不同时长的订单收入（<1小时，1-4小时，>4小时）
     * 3. 时间段内的总收入
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 收入统计数据
     */
    RevenueStatisticsResponse getRevenueStatistics(String startDate, String endDate);

    /**
     * 获取总订单数量（不包括待处理订单）
     * 
     * @return 总订单数量
     */
    int getTotalOrderCount();
}