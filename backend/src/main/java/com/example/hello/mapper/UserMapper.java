package com.example.hello.mapper;

import com.example.hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    @Select("SELECT COUNT(*) FROM Users WHERE username = #{username}")
    int checkUsernameExists(String username);

    @Select("SELECT COUNT(*) FROM Users WHERE email = #{email}")
    int checkEmailExists(String email);

    @Select("SELECT * FROM Users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM Users WHERE user_id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM Users WHERE email = #{email}")
    User findByEmail(String email);

    @Update("UPDATE Users SET username = #{username}, password = #{password}, " +
            "email = #{email}, phone_number = #{phoneNumber} " +
            "WHERE user_id = #{userId}")
    void updateUser(User user);

    @Update("UPDATE Users SET total_usage_hours = total_usage_hours + #{totalUsageHours}, " +
            "total_spent = total_spent + #{totalSpent} " +
            "WHERE user_id = #{userId}")
    void updateUserUsageStats(Integer userId, Float totalUsageHours, BigDecimal totalSpent);
}