package com.parking.repository;

import com.parking.entity.User;
import com.parking.entity.VehicleRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRecordRepository
        extends JpaRepository<VehicleRecord, Long> {

    Optional<VehicleRecord>
    findByVehicleNumberAndExitedFalse(
            String vehicleNumber
    );

    boolean existsByVehicleNumberAndExitedFalse(
            String vehicleNumber
    );

    List<VehicleRecord>
    findByExitedFalse();

    List<VehicleRecord>
    findAllByOrderByEntryTimeDesc();

    List<VehicleRecord>
    findByUserOrderByEntryTimeDesc(
            User user
    );

    List<VehicleRecord>
    findByUserAndExitedFalse(
            User user
    );

    long countByExitedFalse();

    // =========================================================
    // DATE-WISE VEHICLE REPORT
    // =========================================================

    long countByEntryTimeGreaterThanEqualAndEntryTimeLessThan(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );
}
