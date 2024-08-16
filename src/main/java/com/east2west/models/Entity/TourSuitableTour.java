package com.east2west.models.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "toursuitabletour")
public class TourSuitableTour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "toursuitabletourid")
    private int toursuitabletourid;

    public int getToursuitabletourid() {
        return this.toursuitabletourid;
    }

    public void setToursuitabletourid(int toursuitabletourid) {
        this.toursuitabletourid = toursuitabletourid;
    }

    @Column(name = "packageid")
    private int packageid;

    public int getPackageid() {
        return this.packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suitabletourid", referencedColumnName = "suitabletourid")
    private SuitableTour suitabletourid;

    public SuitableTour getSuitabletourid() {
        return this.suitabletourid;
    }

    public void setSuitabletourid(SuitableTour suitabletourid) {
        this.suitabletourid = suitabletourid;
    }

}