package com.example.hello.mapper;

import com.example.hello.entity.CreditCard;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 银行卡Mapper接口
 * 提供银行卡相关的数据库操作
 */
@Mapper
public interface CreditCardMapper {
    
    /**
     * 添加银行卡
     * 
     * @param creditCard 银行卡对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO CreditCards (user_id, card_number, security_code, expiry_date, country) " +
            "VALUES (#{userId}, #{cardNumber}, #{securityCode}, #{expiryDate}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "cardId")
    int addCreditCard(CreditCard creditCard);
    
    /**
     * 根据用户ID查询银行卡列表
     * 
     * @param userId 用户ID
     * @return 银行卡列表
     */
    @Select("SELECT * FROM CreditCards WHERE user_id = #{userId}")
    @Results({
        @Result(property = "cardId", column = "card_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "cardNumber", column = "card_number"),
        @Result(property = "securityCode", column = "security_code"),
        @Result(property = "expiryDate", column = "expiry_date"),
        @Result(property = "country", column = "country")
    })
    List<CreditCard> findByUserId(Integer userId);
    
    /**
     * 根据ID查询银行卡
     * 
     * @param cardId 银行卡ID
     * @return 银行卡对象
     */
    @Select("SELECT * FROM CreditCards WHERE card_id = #{cardId}")
    @Results({
        @Result(property = "cardId", column = "card_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "cardNumber", column = "card_number"),
        @Result(property = "securityCode", column = "security_code"),
        @Result(property = "expiryDate", column = "expiry_date"),
        @Result(property = "country", column = "country")
    })
    CreditCard findById(Integer cardId);
    
    /**
     * 检查银行卡号是否已经存在
     * 
     * @param cardNumber 银行卡号
     * @return 存在的数量
     */
    @Select("SELECT COUNT(*) FROM CreditCards WHERE card_number = #{cardNumber}")
    int countByCardNumber(String cardNumber);
    
    /**
     * 根据ID删除银行卡
     * 
     * @param cardId 银行卡ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM CreditCards WHERE card_id = #{cardId}")
    int deleteById(Integer cardId);
    
    /**
     * 检查银行卡是否属于指定用户
     * 
     * @param cardId 银行卡ID
     * @param userId 用户ID
     * @return 匹配的数量
     */
    @Select("SELECT COUNT(*) FROM CreditCards WHERE card_id = #{cardId} AND user_id = #{userId}")
    int checkCardBelongsToUser(@Param("cardId") Integer cardId, @Param("userId") Integer userId);
} 