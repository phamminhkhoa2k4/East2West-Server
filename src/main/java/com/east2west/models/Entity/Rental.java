package com.east2west.models.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;


@Entity
@Table(name = "Rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rentalid")
    private int rentalid;

    public int getRentalid() {
        return this.rentalid;
    }

    public void setRentalid(int rentalid) {
        this.rentalid = rentalid;
    }

    //  @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "userid", referencedColumnName = "userid")
    // private User user;

    // public User getUser() {
    //     return this.user;
    // }

    // public void setUser(User user) {
    //     this.user = user;
    // }
    @Column(name = "userid")
    private int userid;

    public int getUserid() {
        return this.userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

   

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carid", referencedColumnName = "carid")
    private Car car;

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    private Payment payment;

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Column(name = "rentaldate")
    private Date rentalDate;

    public Date getRentalDate() {
        return this.rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    @Column(name = "returndate")
    private Date returnDate;

    public Date getReturnDate() {
        return this.returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Column(name = "totalamount")
    private double totalAmount;

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

   
}