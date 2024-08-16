package com.east2west.models.Entity;

import jakarta.persistence.*;

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

    @Column(name = "departure_date")
    private String departure_date;

    public String getDeparture_date() {
        return this.departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    @Column(name = "departure_from")
    private String departure_from;

    public String getDeparture_from() {
        return this.departure_from;
    }

    public void setDeparture_from(String departure_from) {
        this.departure_from = departure_from;
    }

    @Column(name = "departure_to")
    private String departure_to;

    public String getDeparture_to() {
        return this.departure_to;
    }

    public void setDeparture_to(String departure_to) {
        this.departure_to = departure_to;
    }

}