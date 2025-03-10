package com.example.hello.controller;

import com.example.hello.common.Result;
import com.example.hello.entity.Scooter;
import com.example.hello.service.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    @Autowired
    private ScooterService scooterService;

    @GetMapping
    public Result<List<Scooter>> findAllAvailable() {
        try {
            List<Scooter> scooters = scooterService.findAllAvailable();
            return Result.success(scooters);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 