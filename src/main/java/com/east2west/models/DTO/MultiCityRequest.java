package com.east2west.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiCityRequest {
    private List<FlightSegment> segments;
}

