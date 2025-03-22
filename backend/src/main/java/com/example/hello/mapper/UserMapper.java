package com.example.hello.mapper;

import com.example.hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("UPDATE Users SET username = #{username}, password = #{password}, " +
            "email = #{email}, phone_number = #{phoneNumber} " +
            "WHERE user_id = #{userId}")
    void updateUser(User user);
}