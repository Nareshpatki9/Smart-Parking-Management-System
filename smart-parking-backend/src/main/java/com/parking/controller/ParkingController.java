package com.parking.controller;

import com.parking.entity.ParkingSlot;
import com.parking.service.ParkingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(
            ParkingService parkingService) {

        this.parkingService = parkingService;
    }

    // =====================================================
    // INITIALIZE PARKING SLOTS
    // =====================================================

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeSlots() {

        parkingService.initializeSlots();

        return ResponseEntity.ok(
                "Parking slots initialized successfully"
        );
    }

    // =====================================================
    // GET ALL PARKING SLOTS
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<ParkingSlot>>
    getAllSlots() {

        return ResponseEntity.ok(
                parkingService.getAllSlots()
        );
    }

    // =====================================================
    // GET AVAILABLE PARKING SLOTS
    // =====================================================

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSlot>>
    getAvailableSlots() {

        return ResponseEntity.ok(
                parkingService.getAvailableSlots()
        );
    }

    // =====================================================
    // GET OCCUPIED PARKING SLOTS
    // =====================================================

    @GetMapping("/occupied")
    public ResponseEntity<List<ParkingSlot>>
    getOccupiedSlots() {

        return ResponseEntity.ok(
                parkingService.getOccupiedSlots()
        );
    }

    // =====================================================
    // PARKING SUMMARY
    // =====================================================

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>>
    getParkingSummary() {

        long totalSlots =
                parkingService.getTotalSlots();

        long availableSlots =
                parkingService.getAvailableSlotCount();

        long occupiedSlots =
                parkingService.getOccupiedSlotCount();

        Map<String, Long> summary =
                new HashMap<>();

        summary.put(
                "totalSlots",
                totalSlots
        );

        summary.put(
                "availableSlots",
                availableSlots
        );

        summary.put(
                "occupiedSlots",
                occupiedSlots
        );

        return ResponseEntity.ok(summary);
    }
}
