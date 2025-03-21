package com.example.hello.mapper;

import com.example.hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}