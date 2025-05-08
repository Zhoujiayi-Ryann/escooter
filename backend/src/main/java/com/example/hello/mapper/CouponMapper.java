package com.example.hello.mapper;

import com.example.hello.entity.Coupon;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 优惠券数据访问接口
 */
@Mapper
public interface CouponMapper {

    /**
     * 获取所有优惠券
     */
    @Select("SELECT * FROM Coupons")
    @Results({
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "couponName", column = "coupon_name"),
        @Result(property = "minSpend", column = "min_spend"),
        @Result(property = "couponAmount", column = "coupon_amount"),
        @Result(property = "validFrom", column = "valid_from"),
        @Result(property = "validTo", column = "valid_to"),
        @Result(property = "isActive", column = "is_active")
    })
    List<Coupon> getAllCoupons();

    /**
     * 插入新优惠券
     */
    @Insert("INSERT INTO Coupons (coupon_name, coupon_amount, min_spend, valid_from, valid_to, is_active) " +
            "VALUES (#{couponName}, #{couponAmount}, #{minSpend}, #{validFrom}, #{validTo}, 1)")
    int insertCoupon(Coupon coupon);

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
            "WHERE user_id = #{userId} AND coupon_id = #{couponId} AND is_used = 0 " +
            "LIMIT 1")
    int useCoupon(@Param("userId") Integer userId, @Param("couponId") Integer couponId, @Param("orderId") Integer orderId);

    /**
     * 查询优惠券详情
     *
     * @param couponId 优惠券ID
     * @return 优惠券详情
     */
    @Select("SELECT * FROM Coupons WHERE coupon_id = #{couponId}")
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

    /**
     * 检查用户是否拥有指定的优惠券且未使用
     *
     * @param userId   用户ID
     * @param couponId 优惠券ID
     * @return 如果用户拥有该优惠券且未使用则返回true，否则返回false
     */
    @Select("SELECT COUNT(*) > 0 FROM Users_Coupons " +
            "WHERE user_id = #{userId} AND coupon_id = #{couponId} AND is_used = 0 " +
            "LIMIT 1")
    boolean checkUserCoupon(@Param("userId") Integer userId, @Param("couponId") Integer couponId);

    /**
     * 批量插入用户优惠券关系
     */
    @Insert("<script>" +
            "INSERT INTO Users_Coupons (user_id, coupon_id, is_used) VALUES " +
            "<foreach collection='userIds' item='userId' separator=','>" +
            "(#{userId}, #{couponId}, 0)" +
            "</foreach>" +
            "</script>")
    int batchDistributeCoupons(@Param("couponId") Integer couponId, @Param("userIds") List<Integer> userIds);

    /**
     * 停用优惠券（软删除）
     */
    @Update("UPDATE Coupons SET is_active = 0 WHERE coupon_id = #{couponId}")
    int deactivateCoupon(@Param("couponId") Integer couponId);

    /**
     * 激活优惠券
     */
    @Update("UPDATE Coupons SET is_active = 1 WHERE coupon_id = #{couponId}")
    int activateCoupon(@Param("couponId") Integer couponId);

    /**
     * 检查用户是否已经拥有该优惠券
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM Users_Coupons WHERE coupon_id = #{couponId} AND user_id IN " +
            "<foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>" +
            "</script>")
    int checkExistingUserCoupons(@Param("couponId") Integer couponId, @Param("userIds") List<Integer> userIds);

    /**
     * 批量检查用户ID是否有效
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM Users WHERE user_id IN " +
            "<foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>" +
            "</script>")
    int checkValidUserIds(@Param("userIds") List<Integer> userIds);
} 