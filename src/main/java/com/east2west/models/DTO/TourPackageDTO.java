package com.east2west.models.DTO;

import java.math.BigDecimal;
import java.util.List;

public class TourPackageDTO {

    private String title;
    private String thumbnail;
    private BigDecimal price;
    private BigDecimal pricereduce;
    private String groupsize;
    private String deposit;
    private String bookinghold;
    private String bookingchange;

    private List<String> categoryTourNames;
    private List<String> themeTourNames;
    private List<String> suitableTourNames;
    private List<String> departureDateNames;

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

    public List<String> getCategoryTourNames() {
        return this.categoryTourNames;
    }

    public void setCategoryTourNames(List<String> categoryTourNames) {
        this.categoryTourNames = categoryTourNames;
    }

    public List<String> getThemeTourNames() {
        return this.themeTourNames;
    }

    public void setThemeTourNames(List<String> themeTourNames) {
        this.themeTourNames = themeTourNames;
    }

    public List<String> getSuitableTourNames() {
        return this.suitableTourNames;
    }

    public void setSuitableTourNames(List<String> suitableTourNames) {
        this.suitableTourNames = suitableTourNames;
    }

    public List<String> getDepartureDateNames() {
        return this.departureDateNames;
    }

    public void setDepartureDateNames(List<String> departureDateNames) {
        this.departureDateNames = departureDateNames;
    }

    // Getters and Setters
    // ...
}