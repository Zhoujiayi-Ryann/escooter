package com.example.hello.service;

import com.example.hello.entity.Scooter;
import java.util.List;

public interface ScooterService {
    List<Scooter> findAllAvailable();
} 