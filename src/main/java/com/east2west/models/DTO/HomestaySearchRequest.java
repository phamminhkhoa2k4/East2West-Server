package com.east2west.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestaySearchRequest {

    private Double longitude;
    private Double latitude;
    private Double radius;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guests;
    private String status;

}