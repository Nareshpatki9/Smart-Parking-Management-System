package com.parking.service;

import com.parking.entity.Bill;
import com.parking.entity.User;
import com.parking.repository.BillRepository;
import com.parking.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public BillingService(
            BillRepository billRepository,
            UserRepository userRepository) {

        this.billRepository =
                billRepository;

        this.userRepository =
                userRepository;
    }

    // =====================================================
    // GET BILLING HISTORY
    //
    // USER  -> Only own bills
    // ADMIN -> All bills
    // =====================================================

    public List<Bill> getBills(
            Authentication authentication) {

        String username =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        // -------------------------------------------------
        // ADMIN -> ALL BILLS
        // -------------------------------------------------

        if (isAdmin) {

            return billRepository
                    .findAllByOrderByPaymentTimeDesc();
        }

        // -------------------------------------------------
        // USER -> OWN BILLS
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );

        return billRepository
                .findByUserOrderByPaymentTimeDesc(
                        user
                );
    }

    // =====================================================
    // GET BILLS BY VEHICLE NUMBER
    //
    // USER  -> Only own vehicle bills
    // ADMIN -> Any vehicle bills
    // =====================================================

    public List<Bill> getBillsByVehicleNumber(
            String vehicleNumber,
            Authentication authentication) {

        if (vehicleNumber == null ||
                vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number is required"
            );
        }

        String normalizedVehicleNumber =
                vehicleNumber
                        .trim()
                        .toUpperCase();

        String username =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        // -------------------------------------------------
        // ADMIN -> ANY VEHICLE
        // -------------------------------------------------

        if (isAdmin) {

            return billRepository
                    .findByVehicleNumber(
                            normalizedVehicleNumber
                    );
        }

        // -------------------------------------------------
        // USER -> OWN VEHICLES ONLY
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );

        return billRepository
                .findByVehicleNumber(
                        normalizedVehicleNumber
                )
                .stream()
                .filter(bill ->
                        bill.getUser() != null &&
                        bill.getUser()
                                .getId()
                                .equals(user.getId())
                )
                .toList();
    }

    // =====================================================
    // GET TOTAL BILLING
    //
    // USER  -> Own paid bills
    // ADMIN -> All paid bills
    // =====================================================

    public double getTotalBilling(
            Authentication authentication) {

        List<Bill> bills =
                getBills(authentication);

        return bills.stream()
                .filter(Bill::isPaid)
                .mapToDouble(Bill::getAmount)
                .sum();
    }
}