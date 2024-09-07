package com.east2west.models.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TourPackageDTO {
    private int id;
    private String title;
    private String thumbnail;
    private BigDecimal price;
    private BigDecimal pricereduce;
    private String groupsize;
    private String deposit;
    private String bookinghold;
    private String bookingchange;
    private List<Integer> categoryTourId;
    private List<Integer> themeTourId;
    private List<Integer> suitableTourId;
    private List<DepartureDateDTO> departureDates;

    // Getters and setters for all fields...

    public static class DepartureDateDTO {
        private String dateTime;

        // Constructor to handle string deserialization
        public DepartureDateDTO(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public Timestamp toTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            LocalDateTime localDateTime = LocalDateTime.parse(this.dateTime, formatter);
            return Timestamp.from(localDateTime.toInstant(ZoneOffset.UTC));
        }
    }

    // Getters and Setters for departureDates and other fields...
    public List<DepartureDateDTO> getDepartureDates() {
        return departureDates;
    }

    public void setDepartureDates(List<DepartureDateDTO> departureDates) {
        this.departureDates = departureDates;
    }

    public List<Integer> getCategoryTourId() {
        return this.categoryTourId;
    }

    public void setCategoryTourId(List<Integer> categoryTourId) {
        this.categoryTourId = categoryTourId;
    }

    public List<Integer> getThemeTourId() {
        return this.themeTourId;
    }

    public void setThemeTourId(List<Integer> themeTourId) {
        this.themeTourId = themeTourId;
    }

    public List<Integer> getSuitableTourId() {
        return this.suitableTourId;
    }

    public void setSuitableTourId(List<Integer> suitableTourId) {
        this.suitableTourId = suitableTourId;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPricereduce() {
        return this.pricereduce;
    }

    public void setPricereduce(BigDecimal pricereduce) {
        this.pricereduce = pricereduce;
    }

    public String getGroupsize() {
        return this.groupsize;
    }

    public void setGroupsize(String groupsize) {
        this.groupsize = groupsize;
    }

    public String getDeposit() {
        return this.deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getBookinghold() {
        return this.bookinghold;
    }

    public void setBookinghold(String bookinghold) {
        this.bookinghold = bookinghold;
    }

    public String getBookingchange() {
        return this.bookingchange;
    }

    public void setBookingchange(String bookingchange) {
        this.bookingchange = bookingchange;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}