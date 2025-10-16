package com.east2west.service;


import com.east2west.models.payload.response.AirportsSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlightService {


    @Value("${amadues.api.key}")
    private String clientId;

    @Value("${amadues.api.secret}")
    private String clientSecret;

    @Value("${amadues.api.token.url}")
    private String tokenUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, entity, Map.class);

        return (String) Objects.requireNonNull(response.getBody()).get("access_token");
    }

        public List<AirportsSearchResponse> searchAirports(String query) {
            String token = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            URI uri = URI.create("https://test.api.amadeus.com/v1/reference-data/locations?subType=AIRPORT&keyword=" + query);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);

            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

            return data.stream().map(item -> {
                AirportsSearchResponse airport = new AirportsSearchResponse();
                airport.setType((String) item.get("type"));
                airport.setSubType((String) item.get("subType"));
                airport.setName((String) item.get("name"));
                airport.setDetailedName((String) item.get("detailedName"));
                airport.setId((String) item.get("id"));
                airport.setTimeZoneOffset((String) item.get("timeZoneOffset"));
                airport.setIataCode((String) item.get("iataCode"));

                // GeoCode
                Map<String, Object> geoCode = (Map<String, Object>) item.get("geoCode");
                if (geoCode != null) {
                    airport.setLatitude(((Number) geoCode.get("latitude")).doubleValue());
                    airport.setLongitude(((Number) geoCode.get("longitude")).doubleValue());
                }

                // Address
                Map<String, Object> address = (Map<String, Object>) item.get("address");
                if (address != null) {
                    airport.setCityName((String) address.get("cityName"));
                    airport.setCityCode((String) address.get("cityCode"));
                    airport.setCountryName((String) address.get("countryName"));
                    airport.setCountryCode((String) address.get("countryCode"));
                    airport.setRegionCode((String) address.get("regionCode"));
                }

                return airport;
            }).collect(Collectors.toList());
        }

}

