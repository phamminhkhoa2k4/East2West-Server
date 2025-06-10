package com.east2west.models.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationDTO {
    private int accommodationid;
    private String accommodationtype;
    private String durationaccommodation;
    private String accommodationname;
    private String accommodationthumbnail;
    private boolean isbreakfast;
    private String roomtype;
}
