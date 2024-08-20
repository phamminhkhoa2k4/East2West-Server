package com.east2west.models.DTO;
public class PlaceDTO {
    private int placeId;
    private String placeName;
    private String placeThumbnail;
    private String description;
    private String placeDuration;

    // Getters and Setters
    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceThumbnail() {
        return placeThumbnail;
    }

    public void setPlaceThumbnail(String placeThumbnail) {
        this.placeThumbnail = placeThumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceDuration() {
        return placeDuration;
    }

    public void setPlaceDuration(String placeDuration) {
        this.placeDuration = placeDuration;
    }
}
