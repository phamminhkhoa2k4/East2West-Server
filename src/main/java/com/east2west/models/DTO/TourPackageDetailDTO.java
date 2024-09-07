package com.east2west.models.DTO;

import java.math.BigDecimal;
import java.util.List;


import com.east2west.models.Entity.*;



    
    public class TourPackageDetailDTO {
        private int packageid;
    private String title;
    private List<String> thumbnail;

    public List<String> getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(List<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    private BigDecimal price;
    private BigDecimal pricereduce;
    private String groupsize;
    private String deposit;
    private String bookinghold;
    private String bookingchange;
    private List<Itinerary> itineraries;
    private List<CategoryTour> categoryTours;
    private List<ThemeTour> themeTours;
    private List<DepartureDate> departureDates;
    private List<SuitableTour> suitableTours;

    public List<DepartureDate> getDepartureDates() {
        return this.departureDates;
    }

    public void setDepartureDates(List<DepartureDate> departureDates) {
        this.departureDates = departureDates;
    }

    public List<SuitableTour> getSuitableTours() {
        return this.suitableTours;
    }

    public void setSuitableTours(List<SuitableTour> suitableTours) {
        this.suitableTours = suitableTours;
    }

    public List<CategoryTour> getCategoryTours() {
        return this.categoryTours;
    }

    public void setCategoryTours(List<CategoryTour> categoryTours) {
        this.categoryTours = categoryTours;
    }

    public List<ThemeTour> getThemeTours() {
        return this.themeTours;
    }

    public void setThemeTours(List<ThemeTour> themeTours) {
        this.themeTours = themeTours;
    }


    
        // Getters and setters
        public int getPackageid() {
            return packageid;
        }
    
        public void setPackageid(int packageid) {
            this.packageid = packageid;
        }
    
        public String getTitle() {
            return title;
        }
    
        public void setTitle(String title) {
            this.title = title;
        }
    
       
        public BigDecimal getPrice() {
            return price;
        }
    
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    
        public BigDecimal getPricereduce() {
            return pricereduce;
        }
    
        public void setPricereduce(BigDecimal pricereduce) {
            this.pricereduce = pricereduce;
        }
    
        public String getGroupsize() {
            return groupsize;
        }
    
        public void setGroupsize(String groupsize) {
            this.groupsize = groupsize;
        }
    
        public String getDeposit() {
            return deposit;
        }
    
        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }
    
        public String getBookinghold() {
            return bookinghold;
        }
    
        public void setBookinghold(String bookinghold) {
            this.bookinghold = bookinghold;
        }
    
        public String getBookingchange() {
            return bookingchange;
        }
    
        public void setBookingchange(String bookingchange) {
            this.bookingchange = bookingchange;
        }
    
        public List<Itinerary> getItineraries() {
            return itineraries;
        }
    
        public void setItineraries(List<Itinerary> itineraries) {
            this.itineraries = itineraries;
        } 
    }
