package com.east2west.models.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MealDTO {
    private int mealid;
    private String mealname;
    private String mealthumbnail;
    private String mealduration;
    private String mealactivity;

    
}

