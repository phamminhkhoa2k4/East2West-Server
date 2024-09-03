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

    @Column(name = "bookingdate")
    private Timestamp bookingdate;

    @Column(name = "feeamount")
    private BigDecimal feeamount;

    @Column(name = "status")
    private String status;

    @Column(name = "numberofguest")
    private int numberofguest;

    @Column(name = "totalprice")
    private BigDecimal totalprice;

    // Getters and Setters

}
