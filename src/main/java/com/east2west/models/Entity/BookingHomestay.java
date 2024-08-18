package com.east2west.models.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
@Entity
@Table(name = "bookinghomestays")
public class BookingHomestay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookinghomestayid")
    private int bookinghomestayid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayavailabilityid", referencedColumnName = "homestayavailabilityid")
    private HomestayAvailability homestayavailability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @Column(name = "checkin")
    private Timestamp checkin;

    @Column(name = "checkout")
    private Timestamp checkout;

    @Column(name = "feeamount")
    private BigDecimal feeamount;

    @Column(name = "status")
    private String status;

    @Column(name = "numberofguest")
    private int numberofguest;

    @Column(name = "totalprice")
    private BigDecimal totalprice;

    // Getters and Setters
    public int getBookinghomestayid() {
        return bookinghomestayid;
    }

    public void setBookinghomestayid(int bookinghomestayid) {
        this.bookinghomestayid = bookinghomestayid;
    }

    public HomestayAvailability getHomestayavailability() {
        return homestayavailability;
    }

    public void setHomestayavailability(HomestayAvailability homestayavailability) {
        this.homestayavailability = homestayavailability;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCheckin() {
        return checkin;
    }

    public void setCheckin(Timestamp checkin) {
        this.checkin = checkin;
    }

    public Timestamp getCheckout() {
        return checkout;
    }

    public void setCheckout(Timestamp checkout) {
        this.checkout = checkout;
    }

    public BigDecimal getFeeamount() {
        return feeamount;
    }

    public void setFeeamount(BigDecimal feeamount) {
        this.feeamount = feeamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberofguest() {
        return numberofguest;
    }

    public void setNumberofguest(int numberofguest) {
        this.numberofguest = numberofguest;
    }

    public BigDecimal getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(BigDecimal totalprice) {
        this.totalprice = totalprice;
    }
}
