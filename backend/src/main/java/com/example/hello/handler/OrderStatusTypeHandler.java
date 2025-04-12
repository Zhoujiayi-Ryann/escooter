package com.example.hello.handler;

import com.example.hello.common.OrderStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OrderStatus枚举类型的自定义处理器
 * 用于处理数据库中的字符串值与Java枚举之间的转换
 */
public class OrderStatusTypeHandler extends BaseTypeHandler<OrderStatus> {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusTypeHandler.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, OrderStatus parameter, JdbcType jdbcType) throws SQLException {
        // 将枚举值转换为字符串并设置到PreparedStatement中
        ps.setString(i, parameter.getValue());
    }

    @Override
    public OrderStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从ResultSet中获取字符串值，并转换为枚举
        String value = rs.getString(columnName);
        return getOrderStatusByValue(value);
    }

    @Override
    public OrderStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从ResultSet中获取字符串值，并转换为枚举
        String value = rs.getString(columnIndex);
        return getOrderStatusByValue(value);
    }

    @Override
    public OrderStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从CallableStatement中获取字符串值，并转换为枚举
        String value = cs.getString(columnIndex);
        return getOrderStatusByValue(value);
    }

    /**
     * 根据字符串值获取对应的OrderStatus枚举值
     * 如果找不到匹配的枚举值，返回PENDING作为默认值并记录警告日志
     */
    private OrderStatus getOrderStatusByValue(String value) {
        if (value == null) {
            return null;
        }
        
        OrderStatus status = OrderStatus.fromValue(value);
        if (status == null) {
            logger.warn("Cannot find matching OrderStatus enum value for '{}', using PENDING as default", value);
            return OrderStatus.PENDING;
        }
        
        return status;
    }
} 