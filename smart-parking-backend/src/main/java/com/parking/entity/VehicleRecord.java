package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_records")
public class VehicleRecord {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // VEHICLE NUMBER
    // Example: MH12AB1234
    // =========================================================

    @Column(
            nullable = false,
            length = 20
    )
    private String vehicleNumber;

    // =========================================================
    // VEHICLE TYPE
    // BIKE / CAR / TRUCK
    // =========================================================

    @Column(
            nullable = false,
            length = 20
    )
    private String vehicleType;

    // =========================================================
    // ENTRY TIME
    // =========================================================

    @Column(nullable = false)
    private LocalDateTime entryTime;

    // =========================================================
    // EXIT TIME
    // NULL while vehicle is still parked
    // =========================================================

    private LocalDateTime exitTime;

    // =========================================================
    // EXIT STATUS
    // false = Active
    // true  = Vehicle exited
    // =========================================================

    @Column(nullable = false)
    private boolean exited;

    // =========================================================
    // PARKING SLOT
    // Example: A1, A2, A3
    // =========================================================

    @Column(length = 20)
    private String slotNumber;

    // =========================================================
    // USER WHO PARKED THE VEHICLE
    // =========================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // Required by JPA
    // =========================================================

    public VehicleRecord() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public VehicleRecord(
            String vehicleNumber,
            String vehicleType,
            LocalDateTime entryTime,
            String slotNumber,
            User user) {

        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.slotNumber = slotNumber;
        this.user = user;
        this.exited = false;
    }

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}