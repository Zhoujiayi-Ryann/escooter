package com.example.hello.dto.response;

import com.example.hello.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 可用优惠券响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCouponsResponse {
    /**
     * 优惠券列表
     */
    private List<Coupon> coupons;
    
    /**
     * 从优惠券列表创建响应
     * @param coupons 优惠券列表
     * @return 可用优惠券响应
     */
    public static AvailableCouponsResponse of(List<Coupon> coupons) {
        return new AvailableCouponsResponse(coupons);
    }
} 