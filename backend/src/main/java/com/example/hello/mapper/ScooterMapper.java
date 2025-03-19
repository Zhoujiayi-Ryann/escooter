package com.example.hello.mapper;

import com.example.hello.entity.Scooter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ScooterMapper {
    /**
     * 查询所有可用的滑板车
     * @return 可用滑板车列表
     */
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, price " +
           "FROM Scooters WHERE status = 'free'")
    List<Scooter> findAllAvailable();
    
    /**
     * 根据ID查询滑板车详细信息
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息
     */
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, " +
           "total_usage_time, price, last_maintenance_date, last_used_date " +
           "FROM Scooters WHERE scooter_id = #{scooterId}")
    Scooter findById(@Param("scooterId") Integer scooterId);
    
    /**
     * 更新滑板车状态
     *
     * @param scooterId 滑板车ID
     * @param status 新状态
     * @return 影响的行数
     */
    @Update("UPDATE Scooters SET status = #{status}, last_used_date = NOW() WHERE scooter_id = #{scooterId}")
    int updateScooterStatus(@Param("scooterId") Integer scooterId, @Param("status") String status);
}