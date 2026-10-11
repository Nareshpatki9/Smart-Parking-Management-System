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
@Table(name = "bills")
public class Bill {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // VEHICLE NUMBER
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
    // BILL AMOUNT
    // =========================================================

    @Column(nullable = false)
    private double amount;

    // =========================================================
    // PARKING DURATION
    // =========================================================

    @Column(nullable = false)
    private long durationHours;

    // =========================================================
    // PAYMENT TIME
    // =========================================================

    @Column(nullable = false)
    private LocalDateTime paymentTime;

    // =========================================================
    // PAYMENT STATUS
    // true = Paid
    // false = Unpaid
    // =========================================================

    @Column(nullable = false)
    private boolean paid;

    // =========================================================
    // USER WHO OWNS THE BILL
    // =========================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // Required by JPA
    // =========================================================

    public Bill() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public Bill(
            String vehicleNumber,
            String vehicleType,
            double amount,
            long durationHours,
            LocalDateTime paymentTime,
            boolean paid,
            User user) {

        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.amount = amount;
        this.durationHours = durationHours;
        this.paymentTime = paymentTime;
        this.paid = paid;
        this.user = user;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(long durationHours) {
        this.durationHours = durationHours;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
