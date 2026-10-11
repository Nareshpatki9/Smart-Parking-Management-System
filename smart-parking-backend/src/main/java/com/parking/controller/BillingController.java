package com.parking.controller;

import com.parking.entity.Bill;
import com.parking.service.BillingService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(
            BillingService billingService) {

        this.billingService = billingService;
    }

    // =====================================================
    // GET BILLING HISTORY
    // USER  -> Only own bills
    // ADMIN -> All bills
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<Bill>> getAllBills(
            Authentication authentication) {

        return ResponseEntity.ok(
                billingService.getBills(
                        authentication
                )
        );
    }

    // =====================================================
    // GET TOTAL BILLING
    // USER  -> Own total
    // ADMIN -> Total revenue
    // =====================================================

    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>>
    getTotalBilling(
            Authentication authentication) {

        double total =
                billingService.getTotalBilling(
                        authentication
                );

        return ResponseEntity.ok(
                Map.of(
                        "totalBilling",
                        total
                )
        );
    }

    // =====================================================
    // GET BILLS BY VEHICLE NUMBER
    // USER  -> Only own vehicle bills
    // ADMIN -> Any vehicle bills
    // =====================================================

    @GetMapping("/{vehicleNumber}")
    public ResponseEntity<List<Bill>>
    getBillsByVehicleNumber(
            @PathVariable String vehicleNumber,
            Authentication authentication) {

        return ResponseEntity.ok(
                billingService.getBillsByVehicleNumber(
                        vehicleNumber,
                        authentication
                )
        );
    }
}
