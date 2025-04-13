package com.example.hello.service;

import com.example.hello.dto.request.ScooterRequest;
import com.example.hello.dto.response.ScooterResponse;
import com.example.hello.entity.Scooter;
import java.util.List;
import java.util.Optional;

/**
 * 滑板车服务接口
 */
public interface ScooterService {
    /**
     * 获取所有滑板车
     *
     * @return 滑板车列表
     */
    List<ScooterResponse> getAllScooters();
    
    /**
     * 查询所有可用的滑板车
     *
     * @return 可用滑板车列表
     */
    List<Scooter> findAllAvailable();
    
    /**
     * 根据ID获取滑板车
     *
     * @param scooterId 滑板车ID
     * @return 滑板车详情
     */
    ScooterResponse getScooterById(Integer scooterId);
    
    /**
     * 根据ID查询滑板车详细信息
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息，如果不存在则返回空
     */
    Optional<Scooter> findById(Integer scooterId);
    
    /**
     * 添加滑板车
     *
     * @param request 滑板车请求DTO
     * @return 新滑板车ID
     */
    Integer addScooter(ScooterRequest request);
    
    /**
     * 更新滑板车信息
     *
     * @param scooterId 滑板车ID
     * @param request 滑板车请求DTO
     * @return 更新后的滑板车信息
     */
    ScooterResponse updateScooter(Integer scooterId, ScooterRequest request);
    
    /**
     * 删除滑板车
     *
     * @param scooterId 滑板车ID
     * @return 是否删除成功
     */
    boolean deleteScooter(Integer scooterId);
}