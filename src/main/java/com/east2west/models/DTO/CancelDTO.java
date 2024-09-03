package com.east2west.models.DTO;

/**
 * RefundDTO
 */
public class CancelDTO {
    int bookingTourId;
    String reasson;

    public String getReasson() {
        return this.reasson;
    }

    public void setReasson(String reasson) {
        this.reasson = reasson;
    }

    public int getBookingTourId() {
        return this.bookingTourId;
    }

    public void setBookingTourId(int bookingTourId) {
        this.bookingTourId = bookingTourId;
    }

    

}