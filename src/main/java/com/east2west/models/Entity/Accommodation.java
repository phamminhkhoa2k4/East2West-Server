package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodationid")
    private int accommodationid;

    public int getAccommodationid() {
        return this.accommodationid;
    }

    public void setAccommodationid(int accommodationid) {
        this.accommodationid = accommodationid;
    }

    @Column(name = "accommodationname")
    private String accommodationname;

    public String getAccommodationname() {
        return this.accommodationname;
    }

    public void setAccommodationname(String accommodationname) {
        this.accommodationname = accommodationname;
    }

    @Column(name = "durationaccommodation")
    private String durationaccommodation;

    public String getDurationaccommodation() {
        return this.durationaccommodation;
    }

    public void setDurationaccommodation(String durationaccommodation) {
        this.durationaccommodation = durationaccommodation;
    }

    @Column(name = "accommodationtype")
    private String accommodationtype;

    public String getAccommodationtype() {
        return this.accommodationtype;
    }

    public void setAccommodationtype(String accommodationtype) {
        this.accommodationtype = accommodationtype;
    }

    
    

   
}