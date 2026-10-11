package com.parking.repository;

import com.parking.entity.ParkingSlot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository
        extends JpaRepository<ParkingSlot, Long> {

    Optional<ParkingSlot> findBySlotNumber(
            String slotNumber
    );

    Optional<ParkingSlot> findFirstByOccupiedFalse();

    List<ParkingSlot> findByOccupiedFalse();

    List<ParkingSlot> findByOccupiedTrue();

    long countByOccupiedFalse();

    long countByOccupiedTrue();
}
