package com.east2west.models.DTO;

import java.sql.Timestamp;
import java.util.List;

public class ItineraryDTO {
    private int itineraryId;
    private int tourPackageId;
    private List<Integer> accommodationIds;
    private List<Integer> mealIds;
    private List<Integer> placeIds;
    private Timestamp day;

    // Getters and Setters
    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public int getTourPackageId() {
        return tourPackageId;
    }

    public void setTourPackageId(int tourPackageId) {
        this.tourPackageId = tourPackageId;
    }

    public List<Integer> getAccommodationIds() {
        return accommodationIds;
    }

    public void setAccommodationIds(List<Integer> accommodationIds) {
        this.accommodationIds = accommodationIds;
    }

    public List<Integer> getMealIds() {
        return mealIds;
    }

    public void setMealIds(List<Integer> mealIds) {
        this.mealIds = mealIds;
    }

    public List<Integer> getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(List<Integer> placeIds) {
        this.placeIds = placeIds;
    }

    public Timestamp getDay() {
        return day;
    }

    public void setDay(Timestamp day) {
        this.day = day;
    }
}
