package com.example.hello.mapper;

import com.example.hello.entity.Scooter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scooter mapper interface
 */
@Mapper
public interface ScooterMapper {
    
    /**
     * Find all scooters
     *
     * @return List of all scooters
     */
    List<Scooter> findAll();
    
    /**
     * Find all available scooters
     *
     * @return List of available scooters
     */
    List<Scooter> findAllAvailable();
    
    /**
     * Find scooters available in a specific time period
     * This excludes scooters that have overlapping orders in the given time period
     *
     * @param startTime Start time of the period
     * @param endTime End time of the period
     * @return List of available scooters in the given time period
     */
    List<Scooter> findAvailableInTimePeriod(@Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find scooter by ID
     *
     * @param scooterId Scooter ID
     * @return Scooter with the given ID, or null if not found
     */
    Scooter findById(@Param("scooterId") Integer scooterId);
    
    /**
     * Check if a scooter exists by ID
     *
     * @param scooterId Scooter ID
     * @return 1 if the scooter exists, 0 otherwise
     */
    int exists(@Param("scooterId") Integer scooterId);
    
    /**
     * Insert a new scooter
     *
     * @param scooter Scooter to insert
     * @return Number of affected rows
     */
    int insert(Scooter scooter);
    
    /**
     * Update a scooter
     *
     * @param scooter Scooter to update
     * @return Number of affected rows
     */
    int update(Scooter scooter);
    
    /**
     * Update scooter status
     *
     * @param scooterId Scooter ID
     * @param status New status
     * @return Number of affected rows
     */
    int updateScooterStatus(@Param("scooterId") Integer scooterId, @Param("status") String status);
    
    /**
     * Soft delete a scooter (mark as deleted)
     *
     * @param scooterId Scooter ID
     * @return Number of affected rows
     */
    int softDelete(@Param("scooterId") Integer scooterId);
    
    /**
     * Hard delete a scooter
     *
     * @param scooterId Scooter ID
     * @return Number of affected rows
     */
    int delete(@Param("scooterId") Integer scooterId);
}