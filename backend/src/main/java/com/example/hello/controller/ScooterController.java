package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.dto.request.ScooterRequest;
import com.example.hello.dto.response.ScooterResponse;
import com.example.hello.entity.Scooter;
import com.example.hello.service.ScooterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 滑板车控制器
 * 提供滑板车相关的API接口
 */
@RestController
@RequestMapping("/api")
public class ScooterController {

    private static final Logger logger = LoggerFactory.getLogger(ScooterController.class);

    @Autowired
    private ScooterService scooterService;

    /**
     * 获取所有滑板车
     *
     * @return 结果包含滑板车列表
     */
    @GetMapping("/scooters_all")
    public Result<List<ScooterResponse>> getAllScooters() {
        logger.info("Get all the scooters");
        try {
            List<ScooterResponse> scooters = scooterService.getAllScooters();
            return Result.success(scooters, "Get the scooter list successfully");
        } catch (Exception e) {
            logger.error("Get the scooter list failed: {}", e.getMessage());
            return Result.error("Get the scooter list failed: " + e.getMessage());
        }
    }

    /**
     * 获取所有可用滑板车
     *
     * @return 结果包含可用滑板车列表
     */
    @GetMapping("/scooters")
    public Result<List<ScooterResponse>> getAvailableScooters() {
        logger.info("Get all the available scooters");
        try {
            List<Scooter> availableScooters = scooterService.findAllAvailable();
            List<ScooterResponse> responseList = availableScooters.stream()
                    .map(ScooterResponse::fromEntity)
                    .collect(Collectors.toList());
            return Result.success(responseList, "Get the available scooter list successfully");
        } catch (Exception e) {
            logger.error("Get the available scooter list failed", e);
            return Result.error("Get the available scooter list failed: " + e.getMessage());
        }
    }

    /**
     * 添加滑板车
     *
     * @param request 滑板车请求DTO
     * @return 结果包含新滑板车ID
     */
    @PostMapping("/scooters")
    public Result<Integer> addScooter(@RequestBody @Valid ScooterRequest request) {
        logger.info("Add the scooter request: {}", request);
        try {
            Integer scooterId = scooterService.addScooter(request);
            return Result.success(scooterId, "Add the scooter successfully");
        } catch (Exception e) {
            logger.error("Add the scooter failed", e);
            return Result.error("Add the scooter failed: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取滑板车信息
     *
     * @param scooterId 滑板车ID
     * @return 结果包含滑板车详细信息
     */
    @GetMapping("/scooters/{scooter_id}")
    public Result<ScooterResponse> getScooterById(@PathVariable("scooter_id") Integer scooterId) {
        logger.info("Get the scooter information request, scooterId: {}", scooterId);
        try {
            ScooterResponse scooter = scooterService.getScooterById(scooterId);
            if (scooter == null) {
                return Result.error("The scooter not found");
            }
            return Result.success(scooter, "Get the scooter information successfully");
        } catch (Exception e) {
            logger.error("Get the scooter information failed", e);
            return Result.error("Get the scooter information failed: " + e.getMessage());
        }
    }

    /**
     * 更新滑板车信息
     *
     * @param scooterId 滑板车ID
     * @param request   滑板车请求DTO
     * @return 结果包含更新后的滑板车信息
     */
    @PostMapping("/scooters/{scooter_id}")
    public Result<ScooterResponse> updateScooter(@PathVariable("scooter_id") Integer scooterId,
                                                @RequestBody @Valid ScooterRequest request) {
        logger.info("Update the scooter information request, scooterId: {}, request: {}", scooterId, request);
        try {
            // 检查滑板车是否存在
            ScooterResponse existingScooter = scooterService.getScooterById(scooterId);
            if (existingScooter == null) {
                return Result.error("The scooter to be updated does not exist");
            }
            
            ScooterResponse updatedScooter = scooterService.updateScooter(scooterId, request);
            return Result.success(updatedScooter, "Update the scooter information successfully");
        } catch (Exception e) {
            logger.error("Update the scooter information failed", e);
            return Result.error("Update the scooter information failed: " + e.getMessage());
        }
    }

    /**
     * 删除滑板车
     *
     * @param scooterId 滑板车ID
     * @return 结果包含操作状态
     */
    @DeleteMapping("/scooters/{scooter_id}")
    public Result<Void> deleteScooter(@PathVariable("scooter_id") Integer scooterId) {
        logger.info("Delete the scooter request, scooterId: {}", scooterId);
        try {
            boolean success = scooterService.deleteScooter(scooterId);
            if (success) {
                return Result.success(null, "Delete the scooter successfully");
            } else {
                return Result.error("The scooter to be deleted does not exist");
            }
        } catch (Exception e) {
            logger.error("Delete the scooter failed", e);
            return Result.error("Delete the scooter failed: " + e.getMessage());
        }
    }
}