package com.east2west.models.DTO;

import org.apache.el.lang.ELArithmetic.BigDecimalDelegate;
import java.math.BigDecimal;
public class TourRevenueDTO {

    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

  

    private String name;
    private BigDecimal revenue;

    public BigDecimal getRevenue() {
        return this.revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    private long bookingcount;

    public long getBookingcount() {
        return this.bookingcount;
    }

    public void setBookingcount(long bookingcount) {
        this.bookingcount = bookingcount;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
    // Getters and Setters

    public TourRevenueDTO(int id, String name, Long bookingcount,BigDecimal revenue) {
        this.id = id;
        this.name = name;
        this.revenue = revenue;
        this.bookingcount=bookingcount;
    }
}
