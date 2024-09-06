package com.east2west.models.DTO;
public class AccommodationDTO {
    private int accommodationid;
    private String accommodationname;
    private String durationaccommodation;
    private String accommodationtype;

    public int getAccommodationid() {
        return this.accommodationid;
    }

    public void setAccommodationid(int accommodationid) {
        this.accommodationid = accommodationid;
    }

    public String getAccommodationname() {
        return this.accommodationname;
    }

    public void setAccommodationname(String accommodationname) {
        this.accommodationname = accommodationname;
    }

    public String getDurationaccommodation() {
        return this.durationaccommodation;
    }

    public void setDurationaccommodation(String durationaccommodation) {
        this.durationaccommodation = durationaccommodation;
    }

    public String getAccommodationtype() {
        return this.accommodationtype;
    }

    public void setAccommodationtype(String accommodationtype) {
        this.accommodationtype = accommodationtype;
    }
    
}
