package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Scooter;
import com.example.hello.service.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 滑板车控制器
 * 提供滑板车相关的API接口
 */
@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    @Autowired
    private ScooterService scooterService;

    /**
     * 获取所有可用的滑板车
     * 
     * @return 可用滑板车列表
     */
    @GetMapping
    public Result<List<Scooter>> findAllAvailable() {
        try {
            List<Scooter> scooters = scooterService.findAllAvailable();
            return Result.success(scooters);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取指定滑板车的详细信息
     * 
     * @param scooterId 滑板车ID
     * @return 滑板车详细信息
     */
    @GetMapping("/{scooter_id}")
    public Result<Scooter> findById(@PathVariable("scooter_id") Integer scooterId) {
        try {
            // 使用Java 8的Optional处理可能的空值
            return scooterService.findById(scooterId)
                    .map(scooter -> Result.success(scooter, "success"))
                    .orElseGet(() -> Result.error("滑板车不存在"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 