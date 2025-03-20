package com.example.hello.mapper;

import com.example.hello.common.OrderStatus;
import com.example.hello.entity.Order;
import com.example.hello.handler.OrderStatusTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 插入新订单
     *
     * @param order 订单对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO Orders (user_id, scooter_id, start_time, end_time, duration, cost, status, extended_duration, discount, address) " +
            "VALUES (#{userId}, #{scooterId}, #{startTime}, #{endTime}, #{duration}, #{cost}, #{status, typeHandler=com.example.hello.handler.OrderStatusTypeHandler}, #{extendedDuration}, #{discount}, #{address})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int insertOrder(Order order);
    
    /**
     * 根据ID查询订单
     *
     * @param orderId 订单ID
     * @return 订单对象
     */
    @Select("SELECT * FROM Orders WHERE order_id = #{orderId}")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "scooterId", column = "scooter_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "cost", column = "cost"),
        @Result(property = "status", column = "status", javaType = OrderStatus.class, typeHandler = OrderStatusTypeHandler.class),
        @Result(property = "extendedDuration", column = "extended_duration"),
        @Result(property = "discount", column = "discount"),
        @Result(property = "address", column = "address")
    })
    Order findById(@Param("orderId") Integer orderId);
    
    /**
     * 获取订单详情，包括关联的滑板车信息
     *
     * @param orderId 订单ID
     * @return 包含订单和滑板车信息的Map
     */
    @Select("SELECT o.order_id, o.user_id, o.scooter_id, o.start_time, o.end_time, o.cost, o.status, o.address, " +
            "s.location_lat, s.location_lng, s.battery_level, s.price " +
            "FROM Orders o " +
            "JOIN Scooters s ON o.scooter_id = s.scooter_id " +
            "WHERE o.order_id = #{orderId}")
    @Results({
        @Result(property = "order_id", column = "order_id"),
        @Result(property = "user_id", column = "user_id"),
        @Result(property = "scooter_id", column = "scooter_id"),
        @Result(property = "start_time", column = "start_time"),
        @Result(property = "end_time", column = "end_time"),
        @Result(property = "cost", column = "cost"),
        @Result(property = "status", column = "status"),
        @Result(property = "address", column = "address"),
        @Result(property = "location_lat", column = "location_lat"),
        @Result(property = "location_lng", column = "location_lng"),
        @Result(property = "battery_level", column = "battery_level"),
        @Result(property = "price", column = "price")
    })
    Map<String, Object> getOrderDetail(@Param("orderId") Integer orderId);
    
    /**
     * 查询需要激活的订单（当前时间大于等于开始时间，状态为paid）
     *
     * @param now 当前时间
     * @return 需要激活的订单列表
     */
    @Select("SELECT * FROM Orders WHERE status = #{status} AND start_time <= #{now}")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "scooterId", column = "scooter_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "cost", column = "cost"),
        @Result(property = "status", column = "status", javaType = OrderStatus.class, typeHandler = OrderStatusTypeHandler.class),
        @Result(property = "extendedDuration", column = "extended_duration"),
        @Result(property = "discount", column = "discount"),
        @Result(property = "address", column = "address")
    })
    List<Order> findOrdersToActivate(@Param("now") LocalDateTime now, @Param("status") String status);
    
    /**
     * 查询需要完成的订单（当前时间大于等于结束时间，状态为active）
     *
     * @param now 当前时间
     * @return 需要完成的订单列表
     */
    @Select("SELECT * FROM Orders WHERE status = #{status} AND end_time <= #{now}")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "scooterId", column = "scooter_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "duration", column = "duration"),
        @Result(property = "cost", column = "cost"),
        @Result(property = "status", column = "status", javaType = OrderStatus.class, typeHandler = OrderStatusTypeHandler.class),
        @Result(property = "extendedDuration", column = "extended_duration"),
        @Result(property = "discount", column = "discount"),
        @Result(property = "address", column = "address")
    })
    List<Order> findOrdersToComplete(@Param("now") LocalDateTime now, @Param("status") String status);
    
    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status 新状态
     * @return 影响的行数
     */
    @Update("UPDATE Orders SET status = #{status} WHERE order_id = #{orderId}")
    int updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);
    
    /**
     * 查询滑板车的小时费率
     *
     * @param scooterId 滑板车ID
     * @return 小时费率
     */
    @Select("SELECT price FROM Scooters WHERE scooter_id = #{scooterId}")
    BigDecimal getScooterPrice(@Param("scooterId") Integer scooterId);
} 