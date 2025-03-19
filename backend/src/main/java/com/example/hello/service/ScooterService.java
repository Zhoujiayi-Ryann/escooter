package com.example.hello.service;

import com.example.hello.entity.Scooter;
import java.util.List;
import java.util.Optional;

public interface ScooterService {
    /**
     * 查询所有可用的滑板车
     * @return 可用滑板车列表
     */
    List<Scooter> findAllAvailable();
    
    /**
     * 根据ID查询滑板车详细信息
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息，如果不存在则返回空
     */
    Optional<Scooter> findById(Integer scooterId);
} 