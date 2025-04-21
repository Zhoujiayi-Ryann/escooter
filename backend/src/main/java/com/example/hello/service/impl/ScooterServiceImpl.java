package com.example.hello.service.impl;

import com.example.hello.dto.request.ScooterAvailabilityRequest;
import com.example.hello.dto.request.ScooterRequest;
import com.example.hello.dto.response.ScooterResponse;
import com.example.hello.entity.Scooter;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.service.ScooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 滑板车服务实现类
 */
@Service
public class ScooterServiceImpl implements ScooterService {
    
    private static final Logger logger = LoggerFactory.getLogger(ScooterServiceImpl.class);
    
    @Autowired
    private ScooterMapper scooterMapper;
    
    /**
     * 获取所有滑板车
     *
     * @return 滑板车列表
     */
    @Override
    public List<ScooterResponse> getAllScooters() {
        logger.info("Get all the scooters");
        List<Scooter> scooters = scooterMapper.findAll();
        return scooters.stream()
                .map(ScooterResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 查询所有可用的滑板车
     *
     * @return 可用滑板车列表
     */
    @Override
    public List<Scooter> findAllAvailable() {
        logger.info("Get all the available scooters");
        // 使用 PageHelper 进行分页，默认每页10条数据
        PageHelper.startPage(1, 10);
        return scooterMapper.findAllAvailable();
    }
    
    /**
     * 根据时间段查询可用的滑板车
     * 
     * @param request 包含时间段的请求对象
     * @return 可用滑板车列表
     */
    @Override
    public List<ScooterResponse> findAvailableInTimePeriod(ScooterAvailabilityRequest request) {
        logger.info("Find available scooters in time period: {}", request);
        return findAvailableInTimePeriod(request.getStart_time(), request.getEnd_time());
    }
    
    /**
     * 根据时间段查询可用的滑板车
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 可用滑板车列表
     */
    @Override
    public List<ScooterResponse> findAvailableInTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Find available scooters from {} to {}", startTime, endTime);
        
        // 验证时间段的有效性
        if (startTime.isAfter(endTime)) {
            logger.warn("Invalid time period: start time is after end time");
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        // 使用 PageHelper 进行分页，默认每页20条数据
        PageHelper.startPage(1, 20);
        List<Scooter> availableScooters = scooterMapper.findAvailableInTimePeriod(startTime, endTime);
        
        logger.info("Found {} available scooters in the specified time period", availableScooters.size());
        
        return availableScooters.stream()
                .map(ScooterResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取滑板车
     *
     * @param scooterId 滑板车ID
     * @return 滑板车详情
     */
    @Override
    public ScooterResponse getScooterById(Integer scooterId) {
        logger.info("Get the scooter information, scooterId: {}", scooterId);
        Scooter scooter = scooterMapper.findById(scooterId);
        if (scooter == null) {
            logger.warn("未找到滑板车, scooterId: {}", scooterId);
            return null;
        }
        return ScooterResponse.fromEntity(scooter);
    }
    
    /**
     * 根据ID查询滑板车详细信息
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息，如果不存在则返回空
     */
    @Override
    public Optional<Scooter> findById(Integer scooterId) {
        logger.info("Get the scooter information, scooterId: {}", scooterId);
        // 使用Java 8的Optional包装，避免空指针异常
        return Optional.ofNullable(scooterMapper.findById(scooterId));
    }
    
    /**
     * 添加滑板车
     *
     * @param request 滑板车请求DTO
     * @return 新滑板车ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addScooter(ScooterRequest request) {
        logger.info("Add the scooter, request: {}", request);
        Scooter scooter = request.toEntity();
        scooterMapper.insert(scooter);
        logger.info("The scooter added successfully, scooterId: {}", scooter.getScooterId());
        return scooter.getScooterId();
    }
    
    /**
     * 更新滑板车信息
     *
     * @param scooterId 滑板车ID
     * @param request 滑板车请求DTO
     * @return 更新后的滑板车信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScooterResponse updateScooter(Integer scooterId, ScooterRequest request) {
        logger.info("Update the scooter information, scooterId: {}, request: {}", scooterId, request);
        
        // 检查滑板车是否存在
        if (scooterMapper.exists(scooterId) == 0) {
            logger.warn("The scooter to be updated does not exist, scooterId: {}", scooterId);
            return null;
        }
        
        // 获取当前滑板车信息
        Scooter existingScooter = scooterMapper.findById(scooterId);
        
        // 创建新的滑板车实体
        Scooter scooter = request.toEntity();
        scooter.setScooterId(scooterId);
        
        // 保留未修改的字段
        if (scooter.getTotalUsageTime() == null || scooter.getTotalUsageTime() == 0) {
            scooter.setTotalUsageTime(existingScooter.getTotalUsageTime());
        }
        
        // 如果状态是维护中，更新最后维护日期
        if (scooter.getStatus() == Scooter.Status.maintenance) {
            scooter.setLastMaintenanceDate(LocalDateTime.now());
        }
        
        // 如果状态是使用中，更新最后使用日期
        if (scooter.getStatus() == Scooter.Status.in_use) {
            scooter.setLastUsedDate(LocalDateTime.now());
        }
        
        // 更新滑板车信息
        scooterMapper.update(scooter);
        
        // 获取更新后的信息
        Scooter updatedScooter = scooterMapper.findById(scooterId);
        logger.info("The scooter information updated successfully, scooterId: {}", scooterId);
        
        return ScooterResponse.fromEntity(updatedScooter);
    }
    
    /**
     * 删除滑板车
     *
     * @param scooterId 滑板车ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteScooter(Integer scooterId) {
        logger.info("Request to delete the scooter, scooterId: {}", scooterId);
        
        // 检查滑板车是否存在
        if (scooterMapper.exists(scooterId) == 0) {
            logger.warn("The scooter to be deleted does not exist or has been deleted, scooterId: {}", scooterId);
            return false;
        }

        // TODO: 检查滑板车是否有关联的订单
        // OrderMapper.countByScooterId(scooterId) > 0
        
        // TODO: 如果有关联订单，可以根据业务需求决定:
        // 1. 拒绝删除并返回错误信息
        // 2. 继续软删除，保留历史数据
        
        // 执行软删除操作
        int rows = scooterMapper.softDelete(scooterId);
        
        boolean success = rows > 0;
        if (success) {
            logger.info("The scooter soft deleted successfully, scooterId: {}", scooterId);
        } else {
            logger.warn("The scooter soft deleted failed, scooterId: {}", scooterId);
        }
        
        return success;
    }
    
    /**
     * 物理删除滑板车（通常不直接调用，仅用于特殊情况）
     *
     * @param scooterId 滑板车ID
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean physicalDeleteScooter(Integer scooterId) {
        logger.info("Request to physical delete the scooter, scooterId: {}", scooterId);
        logger.warn("Physical deletion will permanently delete the record, please use it with caution!");
        
        // 执行物理删除操作
        int rows = scooterMapper.delete(scooterId);
        
        boolean success = rows > 0;
        if (success) {
            logger.info("The scooter physical deleted successfully, scooterId: {}", scooterId);
        } else {
            logger.warn("The scooter physical deleted failed, scooterId: {}", scooterId);
        }
        
        return success;
    }
}