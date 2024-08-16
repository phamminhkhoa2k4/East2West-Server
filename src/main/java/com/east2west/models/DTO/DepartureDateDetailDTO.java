package com.east2west.models.DTO;

import com.east2west.models.Entity.DepartureDate;

public class DepartureDateDetailDTO {
    private int departureDateId;
    private String departure_date;
    private String departure_from;
    private String departure_to;

    // Getters and setters

    public int getDepartureDateId() {
        return departureDateId;
    }

    public void setDepartureDateId(int departureDateId) {
        this.departureDateId = departureDateId;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getDeparture_from() {
        return departure_from;
    }

    public void setDeparture_from(String departure_from) {
        this.departure_from = departure_from;
    }

    public String getDeparture_to() {
        return departure_to;
    }

    public void setDeparture_to(String departure_to) {
        this.departure_to = departure_to;
    }
}