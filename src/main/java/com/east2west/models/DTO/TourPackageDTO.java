package com.east2west.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Setter
@Getter
public class TourPackageDTO {
    private int id;
    private String title;
    private List<String> thumbnail;
    private BigDecimal price;
    private String groupsize;
    private String deposit;
    private String bookinghold;
    private String bookingchange;
    private List<Integer> categoryTourId;
    private List<Integer> themeTourId;
    private List<Integer> suitableTourId;
    private List<DepartureDateDTO> departureDates;

    private List<ItineraryDTO> itinerary;


    @Setter
    @Getter
    public static class DepartureDateDTO {
        private String dateTime;

        // Constructor to handle string deserialization
        public DepartureDateDTO() {
        }
        public DepartureDateDTO(String dateTime) {
            this.dateTime = dateTime;
        }

        public Timestamp toTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            LocalDateTime localDateTime = LocalDateTime.parse(this.dateTime, formatter);
            return Timestamp.from(localDateTime.toInstant(ZoneOffset.UTC));
        }
    }
}