package com.example.hello.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 折扣Mapper接口
 */
@Mapper
public interface DiscountMapper {

    /**
     * 获取用户当前有效的折扣率
     * 如果用户有多个折扣，取折扣率最低的一个（最优惠）
     *
     * @param userId 用户ID
     * @return 折扣率（0-1之间的小数，1表示无折扣）
     */
    @Select("SELECT MIN(discount_rate) FROM Discounts " +
            "WHERE user_id = #{userId} " +
            "AND valid_from <= CURDATE() " +
            "AND (valid_to IS NULL OR valid_to >= CURDATE())")
    BigDecimal getUserDiscountRate(@Param("userId") Integer userId);
} 