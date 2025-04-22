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
     * @param orderId 订单ID
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
     * 获取订单可用的优惠券列表
     * 
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    Optional<AvailableCouponsResponse> getAvailableCoupons(Integer orderId, Integer userId);
}