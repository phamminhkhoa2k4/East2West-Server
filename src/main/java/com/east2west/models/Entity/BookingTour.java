package com.east2west.models.Entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "bookingtour")
public class BookingTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingtourid")
    private int bookingtourid;

    public int getBookingtourid() {
        return this.bookingtourid;
    }

    public void setBookingtourid(int bookingtourid) {
        this.bookingtourid = bookingtourid;
    }

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "packageid", referencedColumnName = "packageid")
    private TourPackage tourpackage;

    public TourPackage getTourpackage() {
        return this.tourpackage;
    }

    public void setTourpackage(TourPackage tourpackage) {
        this.tourpackage = tourpackage;
    }

    @Column(name = "bookingdate")
    private Timestamp bookingdate;

    public Timestamp getBookingdate() {
        return this.bookingdate;
    }

    public void setBookingdate(Timestamp bookingdate) {
        this.bookingdate = bookingdate;
    }

    @Column(name = "tourdate")
    private Timestamp tourdate;

    public Timestamp getTourdate() {
        return this.tourdate;
    }

    public void setTourdate(Timestamp tourdate) {
        this.tourdate = tourdate;
    }

    @Column(name = "numberofpeople")
    private int numberofpeople;

    public int getNumberofpeople() {
        return this.numberofpeople;
    }

    public void setNumberofpeople(int numberofpeople) {
        this.numberofpeople = numberofpeople;
    }

    @Column(name = "tourprice")
    private BigDecimal tourprice;

    public BigDecimal getTourprice() {
        return this.tourprice;
    }

    public void setTourprice(BigDecimal tourprice) {
        this.tourprice = tourprice;
    }

    @Column(name = "depositamount")
    private BigDecimal depositamount;

    public BigDecimal getDepositamount() {
        return this.depositamount;
    }

    public void setDepositamount(BigDecimal depositamount) {
        this.depositamount = depositamount;
    }

    @Column(name = "status")
    private String status;

    @Column(name = "refundamount")
    private BigDecimal refundamount;

    public BigDecimal getRefundamount() {
        return this.refundamount;
    }

    public void setRefundamount(BigDecimal refundamount) {
        this.refundamount = refundamount;
    }

    @Column(name = "refunddate")
    private Timestamp refunddate;

    public Timestamp getRefunddate() {
        return this.refunddate;
    }

    public void setRefunddate(Timestamp refunddate) {
        this.refunddate = refunddate;
    }

    @Column(name = "reason")
    private String reason;

    @Column(name = "depositrefund")
    private boolean depositrefund;

    public boolean isDepositrefund() {
        return this.depositrefund;
    }

    public void setDepositrefund(boolean depositrefund) {
        this.depositrefund = depositrefund;
    }

  



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}