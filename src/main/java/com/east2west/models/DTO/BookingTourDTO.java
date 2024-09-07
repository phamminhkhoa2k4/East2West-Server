package com.east2west.models.DTO;

import java.sql.Date;
import java.math.BigDecimal;

public class BookingTourDTO {
    private int userId;
    private int paymentId;
    private int packageId;
    private Date tourDate;
    private int numberOfPeople;
    private BigDecimal totalPrice;
    private BigDecimal depositAmount;

    // Getters and setters
    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPackageId() {
        return this.packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public Date getTourDate() {
        return this.tourDate;
    }

    public void setTourDate(Date date) {
        this.tourDate = date;
    }

    public int getNumberOfPeople() {
        return this.numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String toParamString() {
        return String.format(
            "Tour Package ID: %d, User ID: %d, Tour Date: %s, Number of People: %d, Total Price: %s",
            packageId, userId, tourDate.toLocalDate().toString(), numberOfPeople, totalPrice.toString()
        );
    }
}
