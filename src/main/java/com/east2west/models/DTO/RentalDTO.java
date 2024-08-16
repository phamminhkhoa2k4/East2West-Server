package com.east2west.models.DTO;

import java.sql.Date;

public class RentalDTO {
    private int userId;
    private int carId;
    private int paymentId;
    private Date rentalDate;
    private Date returnDate;
    private double totalAmount;

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return this.carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getRentalDate() {
        return this.rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
