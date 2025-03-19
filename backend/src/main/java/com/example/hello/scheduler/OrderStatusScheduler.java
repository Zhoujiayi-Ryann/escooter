package com.example.hello.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.hello.mapper.OrderMapper;
import com.example.hello.mapper.ScooterMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 订单状态定时任务
 * 负责根据时间自动更新订单和滑板车状态
 */
@Component
@Slf4j
public class OrderStatusScheduler {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ScooterMapper scooterMapper;

    /**
     * 更新待开始订单的状态
     * 每1分钟执行一次
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void activateOrders() {
        LocalDateTime now = LocalDateTime.now();
        log.info("执行订单激活定时任务，当前时间: {}", now);
        
        try {
            // 查找所有应该开始的订单（当前时间大于等于开始时间，状态为paid）
            orderMapper.findOrdersToActivate(now)
                .forEach(order -> {
                    // 更新订单状态为active
                    orderMapper.updateOrderStatus(order.getOrderId(), "active");
                    // 更新滑板车状态为in_use
                    scooterMapper.updateScooterStatus(order.getScooterId(), "in_use");
                    
                    log.info("订单已激活: {}, 滑板车状态已更新: {}", order.getOrderId(), order.getScooterId());
                });
        } catch (Exception e) {
            log.error("激活订单时发生错误", e);
            throw e;  // 让事务回滚
        }
    }

    /**
     * 结束已完成的订单
     * 每1分钟执行一次
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void completeOrders() {
        LocalDateTime now = LocalDateTime.now();
        log.info("执行订单完成定时任务，当前时间: {}", now);
        
        try {
            // 查找所有应该结束的订单（当前时间大于等于结束时间，状态为active）
            orderMapper.findOrdersToComplete(now)
                .forEach(order -> {
                    // 更新订单状态为completed
                    orderMapper.updateOrderStatus(order.getOrderId(), "completed");
                    // 更新滑板车状态为free
                    scooterMapper.updateScooterStatus(order.getScooterId(), "free");
                    
                    log.info("订单已完成: {}, 滑板车状态已更新为可用: {}", order.getOrderId(), order.getScooterId());
                });
        } catch (Exception e) {
            log.error("完成订单时发生错误", e);
            throw e;  // 让事务回滚
        }
    }
} 