package com.east2west.models.DTO;

import java.sql.Timestamp;
import java.util.List;

public class ItineraryDTO {

    
    private int itineraryId;
    private List<ActivityDTO> activities;
    private Timestamp day;
   

    public List<ActivityDTO> getActivities() {
        return this.activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

   

    // Getters and setters
    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

  
    public Timestamp getDay() {
        return day;
    }

    public void setDay(Timestamp day) {
        this.day = day;
    }
}