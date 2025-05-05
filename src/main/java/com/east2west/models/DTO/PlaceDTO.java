package com.east2west.models.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaceDTO {
    private int placeid;
    private String placename;
    private String placethumbnail;
    private String placeduration;
    private String description;
}
