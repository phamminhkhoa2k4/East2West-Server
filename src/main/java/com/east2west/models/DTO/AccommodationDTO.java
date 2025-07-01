package com.east2west.models.DTO;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDTO {
    private int accommodationid;
    private String durationaccommodation;
    private String accommodationname;
    private String accommodationthumbnail;
    private boolean isbreakfast;
    private String roomtype;
    private String accommodationtype;
}
