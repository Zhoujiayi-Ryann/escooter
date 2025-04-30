package com.example.hello.mapper;

import com.example.hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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
                        "email = #{email}, phone_number = #{phoneNumber}, " +
                        "avatar_path = #{avatarPath} " +
                        "WHERE user_id = #{userId}")
        void updateUser(User user);

        @Update("UPDATE Users SET total_usage_hours = total_usage_hours + #{totalUsageHours}, " +
                        "total_spent = total_spent + #{totalSpent} " +
                        "WHERE user_id = #{userId}")
        void updateUserUsageStats(Integer userId, Float totalUsageHours, BigDecimal totalSpent);

        @Update("UPDATE Users SET is_disabled = #{isDisabled} WHERE user_id = #{userId}")
        void updateUserDisabledStatus(@Param("userId") Integer userId, @Param("isDisabled") Boolean isDisabled);

        /**
         * 查询使用时长超过指定小时数的用户
         * 
         * @param hours 最小使用小时数
         * @return 符合条件的用户列表
         */
        @Select("SELECT * FROM Users WHERE total_usage_hours >= #{hours}")
        @Results({
                        @Result(property = "userId", column = "user_id"),
                        @Result(property = "phoneNumber", column = "phone_number"),
                        @Result(property = "registrationDate", column = "registration_date"),
                        @Result(property = "totalUsageHours", column = "total_usage_hours"),
                        @Result(property = "totalSpent", column = "total_spent"),
                        @Result(property = "userTypes", column = "user_types"),
                        @Result(property = "avatarPath", column = "avatar_path")
        })
        List<User> findFrequentUsers(@Param("hours") float hours);

        /**
         * 获取所有非管理员用户
         * 
         * @return 非管理员用户列表
         */
        @Select("SELECT * FROM Users WHERE user_types IS NULL OR JSON_CONTAINS(user_types, '\"admin\"') = 0")
        @Results({
                        @Result(property = "userId", column = "user_id"),
                        @Result(property = "phoneNumber", column = "phone_number"),
                        @Result(property = "registrationDate", column = "registration_date"),
                        @Result(property = "totalUsageHours", column = "total_usage_hours"),
                        @Result(property = "totalSpent", column = "total_spent"),
                        @Result(property = "userTypes", column = "user_types"),
                        @Result(property = "avatarPath", column = "avatar_path")
        })
        List<User> findAllNonAdminUsers();
}