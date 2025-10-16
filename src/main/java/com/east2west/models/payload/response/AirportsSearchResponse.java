package com.east2west.models.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportsSearchResponse {
    private String type;
    private String subType;
    private String name;
    private String detailedName;
    private String id;
    private String timeZoneOffset;
    private String iataCode;
    private double latitude;
    private double longitude;
    private String cityName;
    private String cityCode;
    private String countryName;
    private String countryCode;
    private String regionCode;
}
