package com.parking.service;

import com.parking.entity.Bill;
import com.parking.entity.ParkingSlot;
import com.parking.entity.User;
import com.parking.entity.VehicleRecord;
import com.parking.exception.ParkingFullException;
import com.parking.exception.VehicleAlreadyExistsException;
import com.parking.exception.VehicleNotAuthorizedException;
import com.parking.exception.VehicleNotFoundException;
import com.parking.repository.BillRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.UserRepository;
import com.parking.repository.VehicleRecordRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

    private final VehicleRecordRepository vehicleRecordRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public VehicleService(
            VehicleRecordRepository vehicleRecordRepository,
            ParkingSlotRepository parkingSlotRepository,
            BillRepository billRepository,
            UserRepository userRepository) {

        this.vehicleRecordRepository = vehicleRecordRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    // =====================================================
    // VEHICLE ENTRY
    // =====================================================

    @Transactional
    public VehicleRecord vehicleEntry(
            String vehicleNumber,
            String vehicleType,
            Authentication authentication) {

        // -------------------------------------------------
        // AUTHENTICATION CHECK
        // -------------------------------------------------

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new VehicleNotAuthorizedException(
                    "User authentication is required"
            );
        }

        // -------------------------------------------------
        // VEHICLE NUMBER VALIDATION
        // -------------------------------------------------

        if (vehicleNumber == null ||
                vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number is required"
            );
        }

        // -------------------------------------------------
        // VEHICLE TYPE VALIDATION
        // -------------------------------------------------

        if (vehicleType == null ||
                vehicleType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle type is required"
            );
        }

        String normalizedVehicleNumber =
                vehicleNumber.trim().toUpperCase();

        String normalizedVehicleType =
                vehicleType.trim().toUpperCase();

        // -------------------------------------------------
        // ALLOWED VEHICLE TYPES
        // -------------------------------------------------

        if (!normalizedVehicleType.equals("BIKE") &&
                !normalizedVehicleType.equals("CAR") &&
                !normalizedVehicleType.equals("TRUCK")) {

            throw new IllegalArgumentException(
                    "Invalid vehicle type. Use BIKE, CAR or TRUCK."
            );
        }

        // -------------------------------------------------
        // CHECK DUPLICATE ACTIVE VEHICLE
        // -------------------------------------------------

        if (vehicleRecordRepository
                .existsByVehicleNumberAndExitedFalse(
                        normalizedVehicleNumber)) {

            throw new VehicleAlreadyExistsException(
                    "Vehicle is already parked: "
                            + normalizedVehicleNumber
            );
        }

        // -------------------------------------------------
        // FIND AVAILABLE PARKING SLOT
        // -------------------------------------------------

        ParkingSlot slot =
                parkingSlotRepository
                        .findFirstByOccupiedFalse()
                        .orElseThrow(() ->
                                new ParkingFullException(
                                        "No parking slot available"
                                )
                        );

        // -------------------------------------------------
        // FIND LOGGED-IN USER
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(
                                authentication.getName()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );

        // -------------------------------------------------
        // OCCUPY PARKING SLOT
        // -------------------------------------------------

        slot.setOccupied(true);

        parkingSlotRepository.save(slot);

        // -------------------------------------------------
        // CREATE VEHICLE RECORD
        // -------------------------------------------------

        VehicleRecord vehicleRecord =
                new VehicleRecord(
                        normalizedVehicleNumber,
                        normalizedVehicleType,
                        LocalDateTime.now(),
                        slot.getSlotNumber(),
                        user
                );

        return vehicleRecordRepository.save(
                vehicleRecord
        );
    }

    // =====================================================
    // VEHICLE EXIT
    // =====================================================

    @Transactional
    public Bill vehicleExit(
            String vehicleNumber,
            Authentication authentication) {

        // -------------------------------------------------
        // AUTHENTICATION CHECK
        // -------------------------------------------------

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new VehicleNotAuthorizedException(
                    "User authentication is required"
            );
        }

        // -------------------------------------------------
        // VEHICLE NUMBER VALIDATION
        // -------------------------------------------------

        if (vehicleNumber == null ||
                vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number is required"
            );
        }

        String normalizedVehicleNumber =
                vehicleNumber.trim().toUpperCase();

        // -------------------------------------------------
        // FIND ACTIVE VEHICLE
        // -------------------------------------------------

        VehicleRecord vehicleRecord =
                vehicleRecordRepository
                        .findByVehicleNumberAndExitedFalse(
                                normalizedVehicleNumber
                        )
                        .orElseThrow(() ->
                                new VehicleNotFoundException(
                                        "Active vehicle not found: "
                                                + normalizedVehicleNumber
                                )
                        );

        String loggedInUsername =
                authentication.getName();

        // -------------------------------------------------
        // CHECK ADMIN ROLE
        // -------------------------------------------------

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        // -------------------------------------------------
        // GET VEHICLE OWNER
        // -------------------------------------------------

        if (vehicleRecord.getUser() == null) {

            throw new VehicleNotFoundException(
                    "Vehicle owner information not found"
            );
        }

        String vehicleOwner =
                vehicleRecord
                        .getUser()
                        .getUsername();

        // -------------------------------------------------
        // USER -> OWN VEHICLE ONLY
        // ADMIN -> ANY VEHICLE
        // -------------------------------------------------

        if (!isAdmin &&
                !vehicleOwner.equals(loggedInUsername)) {

            throw new VehicleNotAuthorizedException(
                    "You are not authorized to exit this vehicle."
            );
        }

        // -------------------------------------------------
        // SET EXIT TIME
        // -------------------------------------------------

        LocalDateTime exitTime =
                LocalDateTime.now();

        vehicleRecord.setExitTime(exitTime);
        vehicleRecord.setExited(true);

        // -------------------------------------------------
        // CALCULATE PARKING DURATION
        // -------------------------------------------------

        long minutes =
                Duration.between(
                        vehicleRecord.getEntryTime(),
                        exitTime
                ).toMinutes();

        long durationHours =
                Math.max(
                        1,
                        (minutes + 59) / 60
                );

        // -------------------------------------------------
        // GET VEHICLE RATE
        // -------------------------------------------------

        double rate =
                getRate(
                        vehicleRecord.getVehicleType()
                );

        // -------------------------------------------------
        // CALCULATE BILL
        // -------------------------------------------------

        double amount =
                durationHours * rate;

        // -------------------------------------------------
        // CREATE BILL
        // -------------------------------------------------

        Bill bill =
                new Bill(
                        vehicleRecord.getVehicleNumber(),
                        vehicleRecord.getVehicleType(),
                        amount,
                        durationHours,
                        exitTime,
                        true,
                        vehicleRecord.getUser()
                );

        // -------------------------------------------------
        // SAVE BILL
        // -------------------------------------------------

        Bill savedBill =
                billRepository.save(bill);

        // -------------------------------------------------
        // SAVE VEHICLE EXIT
        // -------------------------------------------------

        vehicleRecordRepository.save(
                vehicleRecord
        );

        // -------------------------------------------------
        // RELEASE PARKING SLOT
        // -------------------------------------------------

        releaseParkingSlot(
                vehicleRecord.getSlotNumber()
        );

        return savedBill;
    }

    // =====================================================
    // BILLING RATE
    // =====================================================

    private double getRate(
            String vehicleType) {

        if (vehicleType == null ||
                vehicleType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle type is required"
            );
        }

        String normalizedVehicleType =
                vehicleType.trim().toUpperCase();

        return switch (normalizedVehicleType) {

            case "BIKE" -> 20.0;

            case "CAR" -> 50.0;

            case "TRUCK" -> 100.0;

            default ->
                    throw new IllegalArgumentException(
                            "Invalid vehicle type: "
                                    + normalizedVehicleType
                    );
        };
    }

    // =====================================================
    // RELEASE PARKING SLOT
    // =====================================================

    private void releaseParkingSlot(
            String slotNumber) {

        if (slotNumber == null ||
                slotNumber.trim().isEmpty()) {

            return;
        }

        ParkingSlot slot =
                parkingSlotRepository
                        .findBySlotNumber(slotNumber)
                        .orElse(null);

        if (slot != null) {

            slot.setOccupied(false);

            parkingSlotRepository.save(slot);
        }
    }

    // =====================================================
    // GET ALL VEHICLES
    //
    // ADMIN -> All vehicles
    // USER  -> Own vehicles
    // =====================================================

    public List<VehicleRecord> getAllVehicles(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new VehicleNotAuthorizedException(
                    "User authentication is required"
            );
        }

        String username =
                authentication.getName();

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        // -------------------------------------------------
        // ADMIN -> ALL VEHICLES
        // -------------------------------------------------

        if (isAdmin) {

            return vehicleRecordRepository
                    .findAllByOrderByEntryTimeDesc();
        }

        // -------------------------------------------------
        // USER -> OWN VEHICLES
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );

        return vehicleRecordRepository
                .findByUserOrderByEntryTimeDesc(user);
    }

    // =====================================================
    // GET ACTIVE VEHICLES
    //
    // ADMIN -> All active vehicles
    // USER  -> Own active vehicles
    // =====================================================

    public List<VehicleRecord> getActiveVehicles(
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new VehicleNotAuthorizedException(
                    "User authentication is required"
            );
        }

        String username =
                authentication.getName();

        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        // -------------------------------------------------
        // ADMIN -> ALL ACTIVE VEHICLES
        // -------------------------------------------------

        if (isAdmin) {

            return vehicleRecordRepository
                    .findByExitedFalse();
        }

        // -------------------------------------------------
        // USER -> OWN ACTIVE VEHICLES
        // -------------------------------------------------

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Logged-in user not found"
                                )
                        );

        return vehicleRecordRepository
                .findByUserAndExitedFalse(user);
    }
}
