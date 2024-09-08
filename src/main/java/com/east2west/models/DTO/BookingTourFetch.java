package com.east2west.models.DTO;

import com.east2west.models.Entity.BookingTour;
import java.sql.Timestamp;
import java.math.BigDecimal;
public class BookingTourFetch {
    private int bookingTourId;

    public int getBookingTourId() {
        return this.bookingTourId;
    }

    public void setBookingTourId(int bookingTourId) {
        this.bookingTourId = bookingTourId;
    }

    private String tourTitle; // Tiêu đề tour
    private UserFetch user; // Thông tin người dùng
    private String status; // Trạng thái đơn
    private Timestamp bookingDate; // Ngày đặt tour
    private BigDecimal totalAmount; // Tổng số tiền của đặt tour

    public Timestamp getBookingDate() {
        return this.bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
