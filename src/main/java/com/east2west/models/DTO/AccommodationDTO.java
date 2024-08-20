package com.east2west.models.DTO;
public class AccommodationDTO {
    private int accommodationId;
    private String accommodationName;
    private String durationAccommodation;
    private String accommodationType;

    // Getters and Setters
    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public String getDurationAccommodation() {
        return durationAccommodation;
    }

    public void setDurationAccommodation(String durationAccommodation) {
        this.durationAccommodation = durationAccommodation;
    }

    public String getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }
}
