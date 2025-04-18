package com.example.hello.mapper;

import com.example.hello.common.OrderStatus;
import com.example.hello.entity.Order;
import com.example.hello.handler.OrderStatusTypeHandler;
import com.example.hello.dto.response.OrderDetailResponse;
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
        @Insert("INSERT INTO Orders (user_id, scooter_id, start_time, end_time, duration, cost, status, extended_duration, discount, address, create_at) "
                        +
                        "VALUES (#{userId}, #{scooterId}, #{startTime}, #{endTime}, #{duration}, #{cost}, #{status, typeHandler=com.example.hello.handler.OrderStatusTypeHandler}, #{extendedDuration}, #{discount}, #{address}, #{createdAt})")
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
        @Select("SELECT o.*, s.location_lat, s.location_lng, s.battery_level, s.price " +
                        "FROM Orders o " +
                        "LEFT JOIN Scooters s ON o.scooter_id = s.scooter_id " +
                        "WHERE o.order_id = #{orderId} AND o.is_deleted = false")
        @Results({
                        @Result(property = "order_id", column = "order_id"),
                        @Result(property = "user_id", column = "user_id"),
                        @Result(property = "scooter_id", column = "scooter_id"),
                        @Result(property = "start_time", column = "start_time"),
                        @Result(property = "end_time", column = "end_time"),
                        @Result(property = "duration", column = "duration"),
                        @Result(property = "cost", column = "cost"),
                        @Result(property = "status", column = "status"),
                        @Result(property = "extended_duration", column = "extended_duration"),
                        @Result(property = "discount", column = "discount"),
                        @Result(property = "address", column = "address"),
                        @Result(property = "create_at", column = "create_at"),
                        @Result(property = "location_lat", column = "location_lat"),
                        @Result(property = "location_lng", column = "location_lng"),
                        @Result(property = "battery_level", column = "battery_level"),
                        @Result(property = "price", column = "price")
        })
        Map<String, Object> getOrderDetail(@Param("orderId") Integer orderId);

        /**
         * 查询指定滑板车在指定时间段内是否有重叠的订单
         * 只查询状态为pending、paid、active的订单
         *
         * @param scooterId 滑板车ID
         * @param startTime 开始时间
         * @param endTime   结束时间
         * @return 重叠的订单列表
         */
        @Select("SELECT * FROM Orders WHERE scooter_id = #{scooterId} " +
                        "AND status IN ('pending', 'paid', 'active') " +
                        "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +
                        "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +
                        "OR (start_time >= #{startTime} AND end_time <= #{endTime}))")
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
        List<Order> findOverlappingOrders(@Param("scooterId") Integer scooterId,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        /**
         * 查询超时的pending订单
         * 创建时间超过15分钟的pending订单
         *
         * @param timeoutMinutes 超时时间（分钟）
         * @return 超时的订单列表
         */
        @Select("SELECT * FROM Orders WHERE status = 'pending' AND create_at <= DATE_SUB(NOW(), INTERVAL #{timeoutMinutes} MINUTE)")
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
                        @Result(property = "address", column = "address"),
                        @Result(property = "createdAt", column = "create_at")
        })
        List<Order> findTimeoutPendingOrders(@Param("timeoutMinutes") int timeoutMinutes);

        /**
         * 查询滑板车的小时费率
         *
         * @param scooterId 滑板车ID
         * @return 小时费率
         */
        @Select("SELECT price FROM Scooters WHERE scooter_id = #{scooterId}")
        BigDecimal getScooterPrice(@Param("scooterId") Integer scooterId);

        /**
         * 删除订单
         * 只能删除状态为pending的订单
         *
         * @param orderId 订单ID
         * @return 影响的行数
         */
        @Delete("DELETE FROM Orders WHERE order_id = #{orderId} AND status = #{status}")
        int deleteOrder(@Param("orderId") Integer orderId, @Param("status") String status);

        /**
         * 更新订单的删除状态
         * 只能更新状态为completed的订单
         *
         * @param orderId   订单ID
         * @param isDeleted 是否删除
         * @return 影响的行数
         */
        @Update("UPDATE Orders SET is_deleted = #{isDeleted} WHERE order_id = #{orderId} AND status = #{status}")
        int updateOrderDeletedStatus(@Param("orderId") Integer orderId, @Param("isDeleted") Boolean isDeleted,
                        @Param("status") String status);

        /**
         * 更新订单状态
         *
         * @param orderId 订单ID
         * @param status  新状态
         * @return 影响的行数
         */
        @Update("UPDATE Orders SET status = #{status} WHERE order_id = #{orderId}")
        int updateOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);

        /**
         * 延长订单时间
         *
         * @param orderId          订单ID
         * @param newEndTime       新的结束时间
         * @param extendedDuration 延长时间（小时）
         * @param newCost          新的费用
         * @return 影响的行数
         */
        @Update("UPDATE Orders SET end_time = #{newEndTime}, extended_duration = #{extendedDuration}, cost = #{newCost} WHERE order_id = #{orderId}")
        int extendOrder(@Param("orderId") Integer orderId,
                        @Param("newEndTime") LocalDateTime newEndTime,
                        @Param("extendedDuration") Float extendedDuration,
                        @Param("newCost") BigDecimal newCost);

        /**
         * 重置延长订单
         * 将new_end_time设为null，并将status恢复为previous_status
         *
         * @param orderId        订单ID
         * @param previousStatus 之前的状态
         * @return 影响的行数
         */
        @Update("UPDATE Orders SET new_end_time = NULL, status = #{previousStatus} WHERE order_id = #{orderId}")
        int resetExtendedOrder(@Param("orderId") Integer orderId, @Param("previousStatus") String previousStatus);

        /**
         * 查询指定滑板车在指定时间之后的下一个订单
         *
         * @param scooterId   滑板车ID
         * @param currentTime 当前时间
         * @return 下一个订单的开始时间，如果没有则返回null
         */
        @Select("SELECT start_time FROM Orders " +
                        "WHERE scooter_id = #{scooterId} " +
                        "AND status IN ('pending', 'paid', 'active') " +
                        "AND start_time > #{currentTime} " +
                        "ORDER BY start_time ASC " +
                        "LIMIT 1")
        LocalDateTime findNextOrderStartTime(@Param("scooterId") Integer scooterId,
                        @Param("currentTime") LocalDateTime currentTime);
}
