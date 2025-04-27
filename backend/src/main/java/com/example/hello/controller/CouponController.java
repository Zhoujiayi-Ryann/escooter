package com.example.hello.controller;

import com.example.hello.entity.Coupon;
import com.example.hello.service.CouponService;
import com.example.hello.common.Result;
import com.example.hello.dto.request.CouponDistributeRequest;
import com.example.hello.dto.response.CouponDistributeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券管理控制器
 */
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    /**
     * 获取所有优惠券
     */
    @GetMapping
    public Result<List<Coupon>> getAllCoupons() {
        logger.info("Received request to get all coupons");
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            return Result.success(coupons);
        } catch (Exception e) {
            logger.error("Failed to get all coupons", e);
            return Result.error("get coupons failed");
        }
    }

    /**
     * 添加新优惠券
     */
    @PostMapping
    public Result<String> addCoupon(@RequestBody Coupon coupon) {
        logger.info("Received request to add new coupon: {}", coupon);
        
        // 基本参数验证
        if (coupon == null) {
            logger.error("Request body is empty");
            return Result.error("Request parameters cannot be empty");
        }
        if (coupon.getCouponName() == null || coupon.getCouponName().trim().isEmpty()) {
            return Result.error("Coupon name cannot be empty");
        }
        if (coupon.getCouponAmount() == null) {
            return Result.error("Coupon amount cannot be empty");
        }
        if (coupon.getMinSpend() == null) {
            return Result.error("Minimum spend cannot be empty");
        }
        if (coupon.getValidFrom() == null || coupon.getValidTo() == null) {
            return Result.error("Coupon validity period cannot be empty");
        }
        
        try {
            boolean success = couponService.addCoupon(coupon);
            if (success) {
                return Result.success("Coupon added successfully");
            } else {
                return Result.error("Coupon addition failed");
            }
        } catch (Exception e) {
            logger.error("Add coupon failed", e);
            return Result.error("System error, coupon addition failed");
        }
    }

    /**
     * 批量发放优惠券
     */
    @PostMapping("/distribute")
    public Result<CouponDistributeResponse> distributeCoupons(@RequestBody CouponDistributeRequest request) {
        logger.info("Received request to distribute coupons: {}", request);
        try {
            if (request.getCouponId() == null || request.getUserIds() == null || request.getUserIds().isEmpty()) {
                return Result.error("Request parameters cannot be empty");
            }
            CouponDistributeResponse response = couponService.distributeCoupons(request.getCouponId(), request.getUserIds());
            return Result.success(response);
        } catch (Exception e) {
            logger.error("Error distributing coupons", e);
            return Result.error("System error, distribution failed");
        }
    }

    /**
     * 停用优惠券
     */
    @PatchMapping("/{id}/deactivate")
    public Result<Coupon> deactivateCoupon(@PathVariable("id") Integer couponId) {
        logger.info("Received request to deactivate coupon, couponId: {}", couponId);
        try {
            Coupon coupon = couponService.deactivateCoupon(couponId);
            if (coupon != null) {
                return Result.success(coupon, "Coupon deactivated successfully");
            }
            return Result.error("Coupon deactivation failed");
        } catch (Exception e) {
            logger.error("Error deactivating coupon", e);
            return Result.error("System error, deactivation failed");
        }
    }

    /**
     * 激活优惠券
     */
    @PatchMapping("/{id}/activate")
    public Result<Coupon> activateCoupon(@PathVariable("id") Integer couponId) {
        logger.info("Received request to activate coupon, couponId: {}", couponId);
        try {
            Coupon coupon = couponService.activateCoupon(couponId);
            if (coupon != null) {
                return Result.success(coupon, "Coupon activated successfully");
            }
            return Result.error("Coupon activation failed");
        } catch (Exception e) {
            logger.error("Error activating coupon", e);
            return Result.error("System error, activation failed");
        }
    }
} 