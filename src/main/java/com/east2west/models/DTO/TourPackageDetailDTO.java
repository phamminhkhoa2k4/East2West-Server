package com.east2west.models.DTO;

import java.math.BigDecimal;
import java.util.List;


import com.east2west.models.Entity.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
    public class TourPackageDetailDTO {
        private int packageid;
    private String title;
    private List<String> thumbnail;

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




    

    }
