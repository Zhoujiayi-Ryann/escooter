package com.example.hello.dto.response;

import com.example.hello.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    /**
     * 优惠券ID
     */
    private Integer id;
    
    /**
     * 优惠券名称
     */
    private String name;
    
    /**
     * 优惠券描述
     */
    private String description;
    
    /**
     * 优惠券类型：1-满减券，2-折扣券，3-立减券
     */
    private Integer type;
    
    /**
     * 折扣值（折扣券时为折扣百分比，满减券和立减券时为减免金额）
     */
    private BigDecimal value;
    
    /**
     * 使用条件金额（满减券时需要订单满足的金额）
     */
    private BigDecimal threshold;
    
    /**
     * 有效期开始时间
     */
    private LocalDate startTime;
    
    /**
     * 有效期结束时间
     */
    private LocalDate endTime;
    
    /**
     * 优惠券状态：0-未使用，1-已使用，2-已过期
     */
    private Integer status;
    
    /**
     * 从Coupon实体转换为CouponResponse
     * 
     * @param coupon 优惠券实体
     * @return CouponResponse
     */
    public static CouponResponse of(Coupon coupon) {
        if (coupon == null) {
            return null;
        }
        
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getCouponId());
        response.setName(coupon.getCouponName());
        response.setDescription(""); // Coupon实体没有description字段，设置为空字符串
        response.setType(1); // Coupon实体没有type字段，默认设置为满减券类型
        response.setValue(coupon.getCouponAmount());
        response.setThreshold(coupon.getMinSpend());
        response.setStartTime(coupon.getValidFrom());
        response.setEndTime(coupon.getValidTo());
        
        // 将String类型的status转换为Integer类型
        Integer statusCode = 0; // 默认未使用
        if (coupon.getIsUsed() != null && coupon.getIsUsed()) {
            statusCode = 1; // 已使用
        } else if (coupon.getStatus() != null && "disable".equals(coupon.getStatus())) {
            statusCode = 2; // 已过期或不可用
        }
        response.setStatus(statusCode);
        
        return response;
    }
    
    /**
     * 从Coupon实体列表转换为CouponResponse列表
     * 
     * @param coupons 优惠券实体列表
     * @return CouponResponse列表
     */
    public static List<CouponResponse> of(List<Coupon> coupons) {
        if (coupons == null) {
            return null;
        }
        
        return coupons.stream()
                .map(CouponResponse::of)
                .collect(Collectors.toList());
    }
} 