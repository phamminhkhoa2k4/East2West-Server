package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "locationtype")
public class LocationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locationtypeid")
    private int locationtypeid;

    public int getLocationtypeid() {
        return this.locationtypeid;
    }

    public void setLocationtypeid(int locationtypeid) {
        this.locationtypeid = locationtypeid;
    }

    @Column(name = "locationtypename")
    private String locationtypename;

    public String getLocationtypename() {
        return this.locationtypename;
    }

    public void setLocationtypename(String locationtypename) {
        this.locationtypename = locationtypename;
    }

}