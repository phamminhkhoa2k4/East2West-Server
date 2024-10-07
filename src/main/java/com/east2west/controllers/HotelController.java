package com.east2west.controllers;

import com.east2west.service.HotelSearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
public class HotelController {

    private final HotelSearchService hotelSearchService;
    private final ObjectMapper objectMapper;

    @Autowired
    public HotelController(HotelSearchService hotelSearchService, ObjectMapper objectMapper) {
        this.hotelSearchService = hotelSearchService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/api/searchHotels")
    public ResponseEntity<JsonNode> searchHotels(
            @RequestParam String query,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "0") int children
    ) {
        try {
            JsonObject jsonObject = hotelSearchService.searchHotels(query, checkInDate, checkOutDate, adults, children);
            JsonNode jsonNode = objectMapper.readTree(jsonObject.toString());
            return ResponseEntity.ok(jsonNode);
        } catch (IOException ex) {
            return ResponseEntity.status(500).body(objectMapper.createObjectNode().put("error", ex.getMessage()));
        }
    }
}
