package com.east2west.models.DTO;

public class TourDepartureDateDTO {
    private int tourDepartureDateId;
    private int packageid;
    private DepartureDateDetailDTO departureDate;

    // Getters and setters

    public int getTourDepartureDateId() {
        return tourDepartureDateId;
    }

    public void setTourDepartureDateId(int tourDepartureDateId) {
        this.tourDepartureDateId = tourDepartureDateId;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public DepartureDateDetailDTO getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(DepartureDateDetailDTO departureDate) {
        this.departureDate = departureDate;
    }
}
