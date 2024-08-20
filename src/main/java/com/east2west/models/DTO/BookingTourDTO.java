package com.east2west.models.DTO;
import java.sql.Timestamp;
import java.math.BigDecimal;
public class BookingTourDTO {
    private int userId;
    private int paymentId;
    private int packageId;
    private Timestamp bookingDate;
    private Timestamp tourDate;
    private int numberOfPeople;
    private BigDecimal tourPrice;
    private BigDecimal depositAmount;
    private String status;
    private BigDecimal refundAmount;
    private Timestamp refundDate;
    private String reason;
    private boolean depositRefund;

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

    public Timestamp getBookingDate() {
        return this.bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
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

    public BigDecimal getTourPrice() {
        return this.tourPrice;
    }

    public void setTourPrice(BigDecimal tourPrice) {
        this.tourPrice = tourPrice;
    }

    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Timestamp getRefundDate() {
        return this.refundDate;
    }

    public void setRefundDate(Timestamp refundDate) {
        this.refundDate = refundDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isDepositRefund() {
        return this.depositRefund;
    }

    public void setDepositRefund(boolean depositRefund) {
        this.depositRefund = depositRefund;
    }

    // Getters and Setters
}