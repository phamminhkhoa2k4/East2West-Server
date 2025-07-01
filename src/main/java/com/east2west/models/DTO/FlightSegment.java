package com.east2west.models.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class FlightSegment {
    private String departure_id;
    private String arrival_id;
    private String date;


}