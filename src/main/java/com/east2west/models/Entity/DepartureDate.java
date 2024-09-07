package com.east2west.models.Entity;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "departuredate")
public class DepartureDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departuredateid")
    private int departuredateid;

    public int getDeparturedateid() {
        return this.departuredateid;
    }

    public void setDeparturedateid(int departuredateid) {
        this.departuredateid = departuredateid;
    }

    @Column(name = "departuredate")
    private Timestamp departuredate;

    public Timestamp getDeparturedate() {
        return this.departuredate;
    }

    public void setDeparturedate(Timestamp departuredate) {
        this.departuredate = departuredate;
    }

   
}