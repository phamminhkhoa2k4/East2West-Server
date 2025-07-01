package com.east2west.models.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDTO {
    private int mealid;
    private String mealname;
    private String mealthumbnail;
    private String mealduration;
    private String mealactivity;

    
}

