package com.east2west.models.DTO;
import java.sql.Date;
import com.east2west.models.Entity.Rental;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
public class RentalFetch {
    private int rentalId;

    public int getRentalId() {
        return this.rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    private String carName; // Tên xe
    private UserFetch user; // Thông tin người dùng
    private String status; // Trạng thái thuê xe
    private Date rentalDate; // Ngày thuê xe
    private double totalAmount;

    public Date getRentalDate() {
        return this.rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    // Tổng số tiền của thuê xe

  

    

    public String getCarName() {
        return this.carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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
