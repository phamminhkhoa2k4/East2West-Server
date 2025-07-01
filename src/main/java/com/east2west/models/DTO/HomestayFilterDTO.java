package com.east2west.models.DTO;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestayFilterDTO {

    private Integer minBeds;
    private Integer maxBeds;
    private Integer minMaxGuest;
    private Integer maxMaxGuest;
    private String type;
    private List<Integer> amenityIds;

}