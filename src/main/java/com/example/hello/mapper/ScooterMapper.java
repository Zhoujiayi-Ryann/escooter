package com.example.hello.mapper;

import com.example.hello.entity.Scooter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ScooterMapper {
    @Select("SELECT scooter_id, location_lat, location_lng, status, battery_level, price " +
           "FROM Scooters WHERE status = 'free'")
    List<Scooter> findAllAvailable();
} 