package com.east2west.models.DTO;
public class TourSuitableTourDTO {
    private int toursuitabletourid;
    private int packageid;
    private SuitableTourDTO suitableTour;

    // Getters và Setters
    public int getToursuitabletourid() {
        return toursuitabletourid;
    }

    public void setToursuitabletourid(int toursuitabletourid) {
        this.toursuitabletourid = toursuitabletourid;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public SuitableTourDTO getSuitableTour() {
        return suitableTour;
    }

    public void setSuitableTour(SuitableTourDTO suitableTour) {
        this.suitableTour = suitableTour;
    }
}