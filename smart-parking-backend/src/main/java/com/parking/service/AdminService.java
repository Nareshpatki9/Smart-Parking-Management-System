package com.parking.service;

import com.parking.dto.AdminDashboardResponse;

import com.parking.entity.Bill;

import com.parking.repository.BillRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.VehicleRecordRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final ParkingSlotRepository parkingSlotRepository;

    private final VehicleRecordRepository vehicleRecordRepository;

    private final BillRepository billRepository;

    public AdminService(
            ParkingSlotRepository parkingSlotRepository,
            VehicleRecordRepository vehicleRecordRepository,
            BillRepository billRepository) {

        this.parkingSlotRepository =
                parkingSlotRepository;

        this.vehicleRecordRepository =
                vehicleRecordRepository;

        this.billRepository =
                billRepository;
    }

    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    public AdminDashboardResponse getDashboard() {

        // -------------------------------------------------
        // TOTAL PARKING SLOTS
        // -------------------------------------------------

        long totalSlots =
                parkingSlotRepository.count();

        // -------------------------------------------------
        // AVAILABLE PARKING SLOTS
        // -------------------------------------------------

        long availableSlots =
                parkingSlotRepository
                        .countByOccupiedFalse();

        // -------------------------------------------------
        // OCCUPIED PARKING SLOTS
        // -------------------------------------------------

        long occupiedSlots =
                parkingSlotRepository
                        .countByOccupiedTrue();

        // -------------------------------------------------
        // CURRENTLY ACTIVE VEHICLES
        // -------------------------------------------------

        long activeVehicles =
                vehicleRecordRepository
                        .countByExitedFalse();

        // -------------------------------------------------
        // TOTAL BILLS
        // -------------------------------------------------

        long totalBills =
                billRepository.count();

        // -------------------------------------------------
        // TOTAL REVENUE
        // Only PAID bills are included
        // -------------------------------------------------

        List<Bill> bills =
                billRepository.findAll();

        double totalRevenue =
                bills.stream()
                        .filter(Bill::isPaid)
                        .mapToDouble(Bill::getAmount)
                        .sum();

        // -------------------------------------------------
        // RETURN DASHBOARD RESPONSE
        // -------------------------------------------------

        return new AdminDashboardResponse(
                totalSlots,
                availableSlots,
                occupiedSlots,
                activeVehicles,
                totalBills,
                totalRevenue
        );
    }

    // =====================================================
    // DATE-WISE ADMIN REPORT
    // =====================================================

    public Map<String, Object> getDateWiseReport(
            LocalDate date) {

        if (date == null) {

            throw new IllegalArgumentException(
                    "Date is required"
            );
        }

        // -------------------------------------------------
        // START OF SELECTED DATE
        // Example:
        // 2026-10-07 00:00:00
        // -------------------------------------------------

        LocalDateTime startDateTime =
                date.atStartOfDay();

        // -------------------------------------------------
        // START OF NEXT DATE
        // Example:
        // 2026-10-08 00:00:00
        // -------------------------------------------------

        LocalDateTime endDateTime =
                date.plusDays(1)
                        .atStartOfDay();

        // -------------------------------------------------
        // TOTAL VEHICLES PARKED ON SELECTED DATE
        // Based on vehicle ENTRY time
        // -------------------------------------------------

        long totalVehicles =
                vehicleRecordRepository
                        .countByEntryTimeGreaterThanEqualAndEntryTimeLessThan(
                                startDateTime,
                                endDateTime
                        );

        // -------------------------------------------------
        // TOTAL BILLS ON SELECTED DATE
        // Based on bill PAYMENT time
        // -------------------------------------------------

        long totalBills =
                billRepository
                        .countByPaymentTimeGreaterThanEqualAndPaymentTimeLessThan(
                                startDateTime,
                                endDateTime
                        );

        // -------------------------------------------------
        // RESPONSE
        // -------------------------------------------------

        return Map.of(
                "date", date.toString(),
                "totalVehicles", totalVehicles,
                "totalBills", totalBills
        );
    }
}