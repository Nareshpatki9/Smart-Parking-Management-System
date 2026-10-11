package com.parking.repository;

import com.parking.entity.Bill;
import com.parking.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BillRepository
        extends JpaRepository<Bill, Long> {

    // =========================================================
    // FIND BILLS BY VEHICLE NUMBER
    // =========================================================

    List<Bill> findByVehicleNumber(
            String vehicleNumber
    );

    // =========================================================
    // FIND ALL BILLS - NEWEST FIRST
    // =========================================================

    List<Bill> findAllByOrderByPaymentTimeDesc();

    // =========================================================
    // FIND USER'S BILLS - NEWEST FIRST
    // =========================================================

    List<Bill> findByUserOrderByPaymentTimeDesc(
            User user
    );

    // =========================================================
    // DATE-WISE BILL REPORT
    // =========================================================

    long countByPaymentTimeGreaterThanEqualAndPaymentTimeLessThan(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );
}
