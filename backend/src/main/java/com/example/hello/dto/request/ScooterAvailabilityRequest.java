package com.example.hello.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Scooter availability request DTO
 * Used to query available scooters in a specific time period
 */
public class ScooterAvailabilityRequest {
    
    /**
     * Start time of the period
     */
    @NotNull(message = "Start time cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime start_time;
    
    /**
     * End time of the period
     */
    @NotNull(message = "End time cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime end_time;
    
    // Getters and Setters
    public LocalDateTime getStart_time() {
        return start_time;
    }
    
    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }
    
    public LocalDateTime getEnd_time() {
        return end_time;
    }
    
    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
    
    @Override
    public String toString() {
        return "ScooterAvailabilityRequest{" +
                "start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
} 