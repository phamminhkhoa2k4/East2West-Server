package com.east2west.models.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
@Entity
@Table(name = "homestayavailability")
public class HomestayAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayavailabilityid")
    private int homestayavailabilityid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    @Column(name = "status")
    private String status;

    @Column(name = "pricepernight")
    private BigDecimal pricepernight;

    public BigDecimal getPricepernight() {
        return this.pricepernight;
    }

    public void setPricepernight(BigDecimal pricepernight) {
        this.pricepernight = pricepernight;
    }

    @Column(name = "date")
    private Timestamp date;

    // Getters and Setters
    public int getHomestayavailabilityid() {
        return homestayavailabilityid;
    }

    public void setHomestayavailabilityid(int homestayavailabilityid) {
        this.homestayavailabilityid = homestayavailabilityid;
    }

    public Homestay getHomestay() {
        return homestay;
    }

    public void setHomestay(Homestay homestay) {
        this.homestay = homestay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

   

