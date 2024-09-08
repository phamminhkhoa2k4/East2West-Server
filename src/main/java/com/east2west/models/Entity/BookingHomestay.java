package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;
@Entity
@Getter
@Setter
@Table(name = "bookinghomestays")
public class BookingHomestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookinghomestayid")
    private int bookinghomestayid;

    public int getBookinghomestayid() {
        return this.bookinghomestayid;
    }

    public void setBookinghomestayid(int bookinghomestayid) {
        this.bookinghomestayid = bookinghomestayid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayavailabilityid", referencedColumnName = "homestayavailabilityid")
    private HomestayAvailability homestayavailability;

    public HomestayAvailability getHomestayavailability() {
        return this.homestayavailability;
    }

    public void setHomestayavailability(HomestayAvailability homestayavailability) {
        this.homestayavailability = homestayavailability;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "checkin")
    private Timestamp checkin;

    public Timestamp getCheckin() {
        return this.checkin;
    }

    public void setCheckin(Timestamp checkin) {
        this.checkin = checkin;
    }

    @Column(name = "checkout")
    private Timestamp checkout;

    public Timestamp getCheckout() {
        return this.checkout;
    }

    public void setCheckout(Timestamp checkout) {
        this.checkout = checkout;
    }

    @Column(name = "bookingdate")
    private Timestamp bookingdate;

    public Timestamp getBookingdate() {
        return this.bookingdate;
    }

    public void setBookingdate(Timestamp bookingdate) {
        this.bookingdate = bookingdate;
    }

    @Column(name = "feeamount")
    private BigDecimal feeamount;

    public BigDecimal getFeeamount() {
        return this.feeamount;
    }

    public void setFeeamount(BigDecimal feeamount) {
        this.feeamount = feeamount;
    }

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "numberofguest")
    private int numberofguest;

    public int getNumberofguest() {
        return this.numberofguest;
    }

    public void setNumberofguest(int numberofguest) {
        this.numberofguest = numberofguest;
    }

    @Column(name = "totalprice")
    private BigDecimal totalprice;

    public BigDecimal getTotalprice() {
        return this.totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }

    // Getters and Setters

}
