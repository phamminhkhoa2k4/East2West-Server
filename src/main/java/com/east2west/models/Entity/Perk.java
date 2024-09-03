package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "perks")
public class Perk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perkid")
    private int amenitiesid;

    public int getAmenitiesid() {
        return this.amenitiesid;
    }

    public void setAmenitiesid(int amenitiesid) {
        this.amenitiesid = amenitiesid;
    }


    @Column(name = "perkname")
    private String amenitiesname;

    public String getAmenitiesname() {
        return this.amenitiesname;
    }

    public void setAmenitiesname(String amenitiesname) {
        this.amenitiesname = amenitiesname;
    }



    // Getters and Setters

}
