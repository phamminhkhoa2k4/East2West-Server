package com.east2west.models.DTO;

public class ThemeTourDTO {
    private int tourThemeTourId;
    private int packageid;
    private ThemeTourDetailDTO themeTour;

    // Getters and setters

    public int getTourThemeTourId() {
        return tourThemeTourId;
    }

    public void setTourThemeTourId(int tourThemeTourId) {
        this.tourThemeTourId = tourThemeTourId;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public ThemeTourDetailDTO getThemeTour() {
        return themeTour;
    }

    public void setThemeTour(ThemeTourDetailDTO themeTour) {
        this.themeTour = themeTour;
    }
   
}
