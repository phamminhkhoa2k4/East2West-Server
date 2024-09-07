package com.east2west.models.DTO;

import com.east2west.models.Entity.BookingTour;
import java.sql.Timestamp;
import java.math.BigDecimal;
public class RefundFetch {
    private int bookingTourId;

    public int getBookingTourId() {
        return this.bookingTourId;
    }

    public void setBookingTourId(int bookingTourId) {
        this.bookingTourId = bookingTourId;
    }

    private String tourTitle; // Tiêu đề tour
    private UserFetch user; // Thông tin người dùng
    private String reason; // Lý do hoàn tiền
    private String status; // Trạng thái hoàn tiền
    private BigDecimal refundAmount; // Số tiền hoàn tiền
    private Timestamp refundDate; // Ngày hoàn tiền

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

    public String getTourTitle() {
        return this.tourTitle;
    }

    public void setTourTitle(String tourTitle) {
        this.tourTitle = tourTitle;
    }

    public UserFetch getUser() {
        return this.user;
    }

    public void setUser(UserFetch user) {
        this.user = user;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
