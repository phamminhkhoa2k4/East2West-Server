package com.east2west.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class HomestayFilterDTO {

    private Integer minBeds;
    private Integer maxBeds;
    private Integer minMaxGuest;
    private Integer maxMaxGuest;
    private String type;
    private List<Integer> amenityIds;

    // Getters and Setters
}