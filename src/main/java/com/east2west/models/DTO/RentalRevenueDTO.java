package com.east2west.models.DTO;

import java.math.BigDecimal;

public class RentalRevenueDTO {
    private int rentalId;
    private String carName;
    private long totalRentals;
    private BigDecimal totalRevenue;

    public int getRentalId() {
        return this.rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getCarName() {
        return this.carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public long getTotalRentals() {
        return this.totalRentals;
    }

    public void setTotalRentals(long totalRentals) {
        this.totalRentals = totalRentals;
    }

    public BigDecimal getTotalRevenue() {
        return this.totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public RentalRevenueDTO() {
    }

    // Parameterized Constructor
    public RentalRevenueDTO(int rentalId, String carName, long totalRentals, BigDecimal totalRevenue) {
        this.rentalId = rentalId;
        this.carName = carName;
        this.totalRentals = totalRentals;
        this.totalRevenue = totalRevenue;
    }
}