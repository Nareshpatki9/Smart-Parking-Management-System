package com.parking.dto;

public class AdminDashboardResponse {

    private long totalSlots;
    private long availableSlots;
    private long occupiedSlots;
    private long activeVehicles;
    private long totalBills;
    private double totalRevenue;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            long totalSlots,
            long availableSlots,
            long occupiedSlots,
            long activeVehicles,
            long totalBills,
            double totalRevenue) {

        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.occupiedSlots = occupiedSlots;
        this.activeVehicles = activeVehicles;
        this.totalBills = totalBills;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(long totalSlots) {
        this.totalSlots = totalSlots;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(long availableSlots) {
        this.availableSlots = availableSlots;
    }

    public long getOccupiedSlots() {
        return occupiedSlots;
    }

    public void setOccupiedSlots(long occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public long getActiveVehicles() {
        return activeVehicles;
    }

    public void setActiveVehicles(long activeVehicles) {
        this.activeVehicles = activeVehicles;
    }

    public long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(long totalBills) {
        this.totalBills = totalBills;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}