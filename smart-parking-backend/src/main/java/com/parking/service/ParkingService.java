package com.parking.service;

import com.parking.config.ParkingConfig;
import com.parking.entity.ParkingSlot;
import com.parking.exception.ParkingFullException;
import com.parking.repository.ParkingSlotRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingService(
            ParkingSlotRepository parkingSlotRepository) {

        this.parkingSlotRepository =
                parkingSlotRepository;
    }

    // =====================================================
    // INITIALIZE PARKING SLOTS
    // =====================================================

    public void initializeSlots() {

        // Do not create duplicate slots
        if (parkingSlotRepository.count() > 0) {
            return;
        }

        for (int i = 1;
             i <= ParkingConfig.TOTAL_SLOTS;
             i++) {

            ParkingSlot slot =
                    new ParkingSlot(
                            "A" + i,
                            false
                    );

            parkingSlotRepository.save(slot);
        }
    }

    // =====================================================
    // GET ALL PARKING SLOTS
    // =====================================================

    public List<ParkingSlot> getAllSlots() {

        return parkingSlotRepository
                .findAll();
    }

    // =====================================================
    // GET AVAILABLE PARKING SLOTS
    // =====================================================

    public List<ParkingSlot> getAvailableSlots() {

        return parkingSlotRepository
                .findByOccupiedFalse();
    }

    // =====================================================
    // GET OCCUPIED PARKING SLOTS
    // =====================================================

    public List<ParkingSlot> getOccupiedSlots() {

        return parkingSlotRepository
                .findByOccupiedTrue();
    }

    // =====================================================
    // GET FIRST AVAILABLE SLOT
    // =====================================================

    public ParkingSlot getAvailableSlot() {

        return parkingSlotRepository
                .findFirstByOccupiedFalse()
                .orElseThrow(() ->
                        new ParkingFullException(
                                "No parking slot available"
                        )
                );
    }

    // =====================================================
    // OCCUPY PARKING SLOT
    // =====================================================

    public ParkingSlot occupySlot() {

        ParkingSlot slot =
                getAvailableSlot();

        slot.setOccupied(true);

        return parkingSlotRepository.save(slot);
    }

    // =====================================================
    // RELEASE PARKING SLOT
    // =====================================================

    public ParkingSlot releaseSlot(
            String slotNumber) {

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Slot number is required"
            );
        }

        ParkingSlot slot =
                parkingSlotRepository
                        .findBySlotNumber(
                                slotNumber.trim().toUpperCase()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Parking slot not found: "
                                                + slotNumber
                                )
                        );

        slot.setOccupied(false);

        return parkingSlotRepository.save(slot);
    }

    // =====================================================
    // TOTAL PARKING SLOTS
    // =====================================================

    public long getTotalSlots() {

        return parkingSlotRepository.count();
    }

    // =====================================================
    // AVAILABLE SLOT COUNT
    // =====================================================

    public long getAvailableSlotCount() {

        return parkingSlotRepository
                .countByOccupiedFalse();
    }

    // =====================================================
    // OCCUPIED SLOT COUNT
    // =====================================================

    public long getOccupiedSlotCount() {

        return parkingSlotRepository
                .countByOccupiedTrue();
    }
}