package com.east2west.models.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDTO {
    private int placeid;
    private String placename;
    private String placethumbnail;
    private String placeduration;
    private String description;
}
