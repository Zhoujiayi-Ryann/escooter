package com.example.hello.scheduler;

import com.example.hello.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 订单超时定时任务
 */
@Component
@Slf4j
public class OrderTimeoutScheduler {

    @Autowired
    private OrderServiceImpl orderService;

    /**
     * 每分钟检查一次超时的pending订单
     */
    @Scheduled(fixedRate = 60000)
    public void checkTimeoutOrders() {
        log.info("开始执行订单超时检查任务");
        try {
            orderService.handleTimeoutPendingOrders();
        } catch (Exception e) {
            log.error("订单超时检查任务执行失败: {}", e.getMessage(), e);
        }
    }
}