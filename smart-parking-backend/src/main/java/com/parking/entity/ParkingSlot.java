package com.parking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // SLOT NUMBER
    // Example: A1, A2, A3
    // =========================================================

    @Column(
            nullable = false,
            unique = true,
            length = 20
    )
    private String slotNumber;

    // =========================================================
    // OCCUPIED STATUS
    // false = Available
    // true  = Occupied
    // =========================================================

    @Column(nullable = false)
    private boolean occupied;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // Required by JPA
    // =========================================================

    public ParkingSlot() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public ParkingSlot(
            String slotNumber,
            boolean occupied) {

        this.slotNumber = slotNumber;
        this.occupied = occupied;
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

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}