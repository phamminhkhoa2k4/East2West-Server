package com.east2west.models.DTO;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import java.math.BigDecimal;
public class TourRevenueDTO {

    private int tourId;
    private String tourTitle;
    private long bookingCount;
    private BigDecimal totalRevenue;
    private BigDecimal totalRefund;

    public int getTourId() {
        return this.tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getTourTitle() {
        return this.tourTitle;
    }

    public void setTourTitle(String tourTitle) {
        this.tourTitle = tourTitle;
    }

    public long getBookingCount() {
        return this.bookingCount;
    }

    public void setBookingCount(long bookingCount) {
        this.bookingCount = bookingCount;
    }

    public BigDecimal getTotalRevenue() {
        return this.totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalRefund() {
        return this.totalRefund;
    }

    public void setTotalRefund(BigDecimal totalRefund) {
        this.totalRefund = totalRefund;
    }

    // Constructor matching parameters used
    public TourRevenueDTO(int tourId, String tourTitle, long bookingCount, BigDecimal totalRevenue, BigDecimal totalRefund) {
        this.tourId = tourId;
        this.tourTitle = tourTitle;
        this.bookingCount = bookingCount;
        this.totalRevenue = totalRevenue;
        this.totalRefund = totalRefund;
    }

}
