package com.east2west.models.DTO;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultiCityRequest {
    private List<FlightSegment> segments;
}

