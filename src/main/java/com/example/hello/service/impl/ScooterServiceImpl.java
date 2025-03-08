package com.example.hello.service.impl;

import com.example.hello.entity.Scooter;
import com.example.hello.mapper.ScooterMapper;
import com.example.hello.service.ScooterService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScooterServiceImpl implements ScooterService {

    @Autowired
    private ScooterMapper scooterMapper;

    @Override
    public List<Scooter> findAllAvailable() {
        // 使用 PageHelper 进行分页，默认每页10条数据
        PageHelper.startPage(1, 10);
        return scooterMapper.findAllAvailable();
    }
} 