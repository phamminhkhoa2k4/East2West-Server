package com.east2west.models.DTO;
import java.sql.Timestamp;
import java.math.BigDecimal;
public class BookingTourDTO {
    private int userId;
    private int paymentId;
    private int packageId;
    private Timestamp tourDate;
    private int numberOfPeople;
    private BigDecimal totalPrice;
    private BigDecimal depositAmount;
    
    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

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



    public Timestamp getTourDate() {
        return this.tourDate;
    }

    public void setTourDate(Timestamp tourDate) {
        this.tourDate = tourDate;
    }

    public int getNumberOfPeople() {
        return this.numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

   
    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }


}