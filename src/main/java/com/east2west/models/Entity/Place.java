package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "placeid")
    private int placeid;

    public int getPlaceid() {
        return this.placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    @Column(name = "placename")
    private String placename;

    public String getPlacename() {
        return this.placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    @Column(name = "placethumbnail")
    private String placethumbnail;

    public String getPlacethumbnail() {
        return this.placethumbnail;
    }

    public void setPlacethumbnail(String placethumbnail) {
        this.placethumbnail = placethumbnail;
    }

    @Column(name = "description")
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "placeduration")
    private String placeduration;

    public String getPlaceduration() {
        return this.placeduration;
    }

    public void setPlaceduration(String placeduration) {
        this.placeduration = placeduration;
    }



}
