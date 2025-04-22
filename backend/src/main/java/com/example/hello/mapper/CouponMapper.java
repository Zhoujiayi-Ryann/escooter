package com.example.hello.mapper;

import com.example.hello.entity.Coupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 优惠券Mapper接口
 */
@Mapper
public interface CouponMapper {

    /**
     * 查询用户所有优惠券（包括已使用和未使用的）
     *
     * @param userId 用户ID
     * @return 优惠券列表
     */
    @Select("SELECT c.*, uc.is_used, uc.order_id " +
            "FROM Coupons c " +
            "JOIN Users_Coupons uc ON c.coupon_id = uc.coupon_id " +
            "WHERE uc.user_id = #{userId} AND c.is_active = 1")
    @Results({
            @Result(property = "couponId", column = "coupon_id"),
            @Result(property = "couponName", column = "coupon_name"),
            @Result(property = "minSpend", column = "min_spend"),
            @Result(property = "couponAmount", column = "coupon_amount"),
            @Result(property = "validFrom", column = "valid_from"),
            @Result(property = "validTo", column = "valid_to"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "isUsed", column = "is_used"),
            @Result(property = "orderId", column = "order_id")
    })
    List<Coupon> findUserCoupons(@Param("userId") Integer userId);

    /**
     * 使用优惠券（设置为已使用状态并关联订单）
     *
     * @param userId   用户ID
     * @param couponId 优惠券ID
     * @param orderId  订单ID
     * @return 影响的行数
     */
    @Update("UPDATE Users_Coupons SET is_used = 1, order_id = #{orderId} " +
            "WHERE user_id = #{userId} AND coupon_id = #{couponId} AND is_used = 0")
    int useCoupon(@Param("userId") Integer userId, @Param("couponId") Integer couponId, @Param("orderId") Integer orderId);

    /**
     * 查询优惠券详情
     *
     * @param couponId 优惠券ID
     * @return 优惠券详情
     */
    @Select("SELECT * FROM Coupons WHERE coupon_id = #{couponId} AND is_active = 1")
    @Results({
            @Result(property = "couponId", column = "coupon_id"),
            @Result(property = "couponName", column = "coupon_name"),
            @Result(property = "minSpend", column = "min_spend"),
            @Result(property = "couponAmount", column = "coupon_amount"),
            @Result(property = "validFrom", column = "valid_from"),
            @Result(property = "validTo", column = "valid_to"),
            @Result(property = "isActive", column = "is_active")
    })
    Coupon findById(@Param("couponId") Integer couponId);
} 