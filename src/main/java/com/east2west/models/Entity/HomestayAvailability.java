package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.math.BigDecimal;
@Entity
@Table(name = "homestayavailability")
public class HomestayAvailability {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayavailabilityid")
    private int homestayavailabilityid;

	public int getHomestayavailabilityid() {
		return this.homestayavailabilityid;
	}

	public void setHomestayavailabilityid(int homestayavailabilityid) {
		this.homestayavailabilityid = homestayavailabilityid;
	}


   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    public Homestay getHomestay() {
        return this.homestay;
    }

    public void setHomestay(Homestay homestay) {
        this.homestay = homestay;
    }

    
    @Column(name = "status")
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
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

    public Timestamp getDate() {
        return this.date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

  

}

   

