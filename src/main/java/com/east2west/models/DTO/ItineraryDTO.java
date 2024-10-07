package com.east2west.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class ItineraryDTO {

    private int itineraryId;
    private int tourPackageId;
    private List<Integer> accommodationIds;
    private List<Integer> mealIds;
    private List<Integer> placeIds;
    private List<Integer> transferIds;

    private int day;









}

