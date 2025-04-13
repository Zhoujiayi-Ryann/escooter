package com.example.hello.mapper;

import com.example.hello.entity.Scooter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScooterMapper {
    /**
     * 查询所有滑板车（未删除的）
     * @return 所有滑板车列表
     */
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, " +
           "total_usage_time, price, last_maintenance_date, last_used_date, is_deleted " +
           "FROM Scooters WHERE is_deleted = 0")
    List<Scooter> findAll();
    
    /**
     * 查询所有可用的滑板车（未删除的）
     * @return 可用滑板车列表
     */
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, price " +
           "FROM Scooters WHERE status = 'free' AND is_deleted = 0")
    List<Scooter> findAllAvailable();
    
    /**
     * 根据ID查询滑板车详细信息（未删除的）
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息
     */
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, " +
           "total_usage_time, price, last_maintenance_date, last_used_date, is_deleted " +
           "FROM Scooters WHERE scooter_id = #{scooterId} AND is_deleted = 0")
    Scooter findById(@Param("scooterId") Integer scooterId);
    
    /**
     * 添加滑板车
     * 
     * @param scooter 滑板车实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO Scooters(location_lat, location_lng, status, battery_level, total_usage_time, price, is_deleted) " +
           "VALUES(#{locationLat}, #{locationLng}, #{status}, #{batteryLevel}, #{totalUsageTime}, #{price}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "scooterId")
    int insert(Scooter scooter);
    
    /**
     * 更新滑板车信息
     * 
     * @param scooter 滑板车实体
     * @return 影响的行数
     */
    @Update("<script>" +
           "UPDATE Scooters " +
           "SET location_lat = #{locationLat}, " +
           "location_lng = #{locationLng}, " +
           "status = #{status}, " +
           "battery_level = #{batteryLevel}, " +
           "total_usage_time = #{totalUsageTime}, " +
           "price = #{price}" +
           "<if test='lastMaintenanceDate != null'>, last_maintenance_date = #{lastMaintenanceDate}</if>" +
           "<if test='lastUsedDate != null'>, last_used_date = #{lastUsedDate}</if>" +
           " WHERE scooter_id = #{scooterId} AND is_deleted = 0" +
           "</script>")
    int update(Scooter scooter);
    
    /**
     * 更新滑板车状态
     *
     * @param scooterId 滑板车ID
     * @param status 新状态
     * @return 影响的行数
     */
    @Update("UPDATE Scooters SET status = #{status}, last_used_date = NOW() WHERE scooter_id = #{scooterId} AND is_deleted = 0")
    int updateScooterStatus(@Param("scooterId") Integer scooterId, @Param("status") String status);
    
    /**
     * 检查滑板车是否存在（未删除的）
     * 
     * @param scooterId 滑板车ID
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM Scooters WHERE scooter_id = #{scooterId} AND is_deleted = 0")
    int exists(@Param("scooterId") Integer scooterId);
    
    /**
     * 软删除滑板车（将is_deleted标记为1）
     * 
     * @param scooterId 滑板车ID
     * @return 影响的行数
     */
    @Update("UPDATE Scooters SET is_deleted = 1 WHERE scooter_id = #{scooterId} AND is_deleted = 0")
    int softDelete(@Param("scooterId") Integer scooterId);
    
    /**
     * 物理删除滑板车（真实删除记录，通常不使用）
     * 
     * @param scooterId 滑板车ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM Scooters WHERE scooter_id = #{scooterId}")
    int delete(@Param("scooterId") Integer scooterId);
}