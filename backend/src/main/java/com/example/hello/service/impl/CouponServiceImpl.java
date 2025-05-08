package com.example.hello.service.impl;

import com.example.hello.entity.Coupon;
import com.example.hello.mapper.CouponMapper;
import com.example.hello.service.CouponService;
import com.example.hello.dto.response.CouponDistributeResponse;
import com.example.hello.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券服务实现类
 */
@Service
public class CouponServiceImpl implements CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Coupon> getAllCoupons() {
        logger.info("Get all coupons");
        try {
            List<Coupon> coupons = couponMapper.getAllCoupons();
            logger.info("Found {} coupons", coupons == null ? 0 : coupons.size());
            if (coupons != null && !coupons.isEmpty()) {
                logger.info("Coupon example: {}", coupons.get(0));
            }
            return coupons;
        } catch (Exception e) {
            logger.error("Error getting coupons: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean addCoupon(Coupon coupon) {
        logger.info("Start adding new coupon: {}", coupon);
        try {
            // 验证必填字段
            if (coupon == null) {
                logger.error("Coupon object is null");
                return false;
            }
            if (coupon.getCouponName() == null || coupon.getCouponName().trim().isEmpty()) {
                logger.error("Coupon name cannot be empty");
                return false;
            }
            if (coupon.getCouponAmount() == null) {
                logger.error("Coupon amount cannot be empty");
                return false;
            }
            if (coupon.getMinSpend() == null) {
                logger.error("Minimum spend cannot be empty");
                return false;
            }
            if (coupon.getValidFrom() == null) {
                logger.error("Coupon start time cannot be empty");
                return false;
            }
            if (coupon.getValidTo() == null) {
                logger.error("Coupon end time cannot be empty");
                return false;
            }

            // 验证金额合理性
            if (coupon.getCouponAmount().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Coupon amount must be greater than 0");
                return false;
            }
            if (coupon.getMinSpend().compareTo(BigDecimal.ZERO) < 0) {
                logger.error("Minimum spend cannot be less than 0");
                return false;
            }

            // 验证时间合理性
            if (coupon.getValidFrom().isAfter(coupon.getValidTo())) {
                logger.error("Coupon start time cannot be after end time");
                return false;
            }

            boolean success = couponMapper.insertCoupon(coupon) > 0;
            if (success) {
                logger.info("Coupon added successfully");
            } else {
                logger.warn("Coupon addition failed, affected rows: 0");
            }
            return success;
        } catch (Exception e) {
            logger.error("Add coupon failed failed: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public CouponDistributeResponse distributeCoupons(Integer couponId, List<Integer> userIds) {
        logger.info("Distribute coupons, couponId: {}, userIds: {}", couponId, userIds);
        try {
            // 验证优惠券是否存在且激活
            Coupon coupon = couponMapper.findById(couponId);
            if (coupon == null) {
                logger.error("Coupon does not exist, couponId: {}", couponId);
                return CouponDistributeResponse.of(0, userIds.size());
            }
            if (!coupon.getIsActive()) {
                logger.error("Coupon is deactivated, couponId: {}", couponId);
                return CouponDistributeResponse.of(0, userIds.size());
            }

            // 验证用户ID是否都有效
            int validUserCount = couponMapper.checkValidUserIds(userIds);
            if (validUserCount != userIds.size()) {
                logger.error("Invalid user IDs, validUserCount: {}, requestUserCount: {}", validUserCount, userIds.size());
                return CouponDistributeResponse.of(0, userIds.size());
            }

            // 批量发放优惠券
            int successCount = couponMapper.batchDistributeCoupons(couponId, userIds);
            int failCount = userIds.size() - successCount;
            
            // 发送优惠券通知
            if (successCount > 0) {
                try {
                    // 将Integer类型的userIds转换为Long类型
                    List<Long> longUserIds = userIds.stream()
                            .map(Long::valueOf)
                            .collect(Collectors.toList());
                    
                    // 批量创建优惠券通知
                    int notificationCount = notificationService.batchCreateCouponNotifications(
                            longUserIds, couponId, coupon.getCouponName());
                    
                    logger.info("Created {} coupon notifications for {} users", notificationCount, successCount);
                } catch (Exception e) {
                    // 通知发送失败不影响优惠券发放结果
                    logger.error("Failed to create coupon notifications: {}", e.getMessage(), e);
                }
            }
            
            logger.info("Coupon distribution completed, success: {}, failed: {}", successCount, failCount);
            return CouponDistributeResponse.of(successCount, failCount);
        } catch (Exception e) {
            logger.error("Error distributing coupons: {}", e.getMessage(), e);
            return CouponDistributeResponse.of(0, userIds.size());
        }
    }

    @Override
    public Coupon deactivateCoupon(Integer couponId) {
        logger.info("Start deactivating coupon, couponId: {}", couponId);
        try {
            int updated = couponMapper.deactivateCoupon(couponId);
            if (updated > 0) {
                Coupon coupon = couponMapper.findById(couponId);
                logger.info("Coupon deactivated successfully, couponId: {}", couponId);
                return coupon;
            }
            logger.error("Coupon deactivation failed, couponId: {}", couponId);
            return null;
        } catch (Exception e) {
            logger.error("Error deactivating coupon: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Coupon activateCoupon(Integer couponId) {
        logger.info("Start activating coupon, couponId: {}", couponId);
        try {
            int updated = couponMapper.activateCoupon(couponId);
            if (updated > 0) {
                Coupon coupon = couponMapper.findById(couponId);
                logger.info("Coupon activated successfully, couponId: {}", couponId);
                return coupon;
            }
            logger.error("Coupon activation failed, couponId: {}", couponId);
            return null;
        } catch (Exception e) {
            logger.error("Error activating coupon: {}", e.getMessage(), e);
            return null;
        }
    }
} 