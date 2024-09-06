package com.east2west.models.DTO;
public class PlaceDTO {
    private int placeid;
    private String placename;
    private String placethumbnail;
    private String description;
    private String placeduration;

    public int getPlaceid() {
        return this.placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    public String getPlacename() {
        return this.placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getPlacethumbnail() {
        return this.placethumbnail;
    }

    public void setPlacethumbnail(String placethumbnail) {
        this.placethumbnail = placethumbnail;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceduration() {
        return this.placeduration;
    }

    public void setPlaceduration(String placeduration) {
        this.placeduration = placeduration;
    }

   
}
