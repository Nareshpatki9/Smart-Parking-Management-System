package com.parking.controller;

import com.parking.entity.Bill;
import com.parking.entity.VehicleRecord;
import com.parking.service.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(
            VehicleService vehicleService) {

        this.vehicleService = vehicleService;
    }

    // =====================================================
    // VEHICLE ENTRY
    // =====================================================

    @PostMapping("/entry")
    public ResponseEntity<?> vehicleEntry(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String vehicleNumber =
                request.get("vehicleNumber");

        String vehicleType =
                request.get("vehicleType");

        VehicleRecord vehicleRecord =
                vehicleService.vehicleEntry(
                        vehicleNumber,
                        vehicleType,
                        authentication
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleRecord);
    }

    // =====================================================
    // VEHICLE EXIT
    // =====================================================

    @PostMapping("/exit/{vehicleNumber}")
    public ResponseEntity<?> vehicleExit(
            @PathVariable("vehicleNumber")
            String vehicleNumber,
            Authentication authentication) {

        Bill bill =
                vehicleService.vehicleExit(
                        vehicleNumber,
                        authentication
                );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Vehicle exited successfully",

                        "vehicleNumber",
                        bill.getVehicleNumber(),

                        "vehicleType",
                        bill.getVehicleType(),

                        "durationHours",
                        bill.getDurationHours(),

                        "amount",
                        bill.getAmount(),

                        "paymentTime",
                        bill.getPaymentTime(),

                        "paid",
                        bill.isPaid()
                )
        );
    }

    // =====================================================
    // VEHICLE HISTORY
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<VehicleRecord>>
    getAllVehicles(
            Authentication authentication) {

        return ResponseEntity.ok(
                vehicleService.getAllVehicles(
                        authentication
                )
        );
    }

    // =====================================================
    // ACTIVE VEHICLES
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<VehicleRecord>>
    getActiveVehicles(
            Authentication authentication) {

        return ResponseEntity.ok(
                vehicleService.getActiveVehicles(
                        authentication
                )
        );
    }
}
