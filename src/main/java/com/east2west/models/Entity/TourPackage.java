package com.east2west.models.Entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "tourpackage")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packageid")
    private int packageid;
    public int getPackageid() {
        return this.packageid;
    }
    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }
    @Column(name = "title")
    private String title;
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Column(name = "price")
    private BigDecimal price;

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "pricereduce")
    private BigDecimal pricereduce;

    public BigDecimal getPricereduce() {
        return this.pricereduce;
    }

    public void setPricereduce(BigDecimal pricereduce) {
        this.pricereduce = pricereduce;
    }

    @Column(name = "groupsize")
    private String groupsize;

    public String getGroupsize() {
        return this.groupsize;
    }

    public void setGroupsize(String groupsize) {
        this.groupsize = groupsize;
    }

    @Column(name = "deposit")
    private String deposit;

    public String getDeposit() {
        return this.deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    @Column(name = "bookinghold")
    private String bookinghold;

    public String getBookinghold() {
        return this.bookinghold;
    }

    public void setBookinghold(String bookinghold) {
        this.bookinghold = bookinghold;
    }

    @Column(name = "bookingchange")
    private String bookingchange;

    public String getBookingchange() {
        return this.bookingchange;
    }

    public void setBookingchange(String bookingchange) {
        this.bookingchange = bookingchange;
    }
    @OneToMany(mappedBy = "tourPackage", fetch = FetchType.LAZY)
    private List<Itinerary> itineraries;

    public List<Itinerary> getItineraries() {
        return this.itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }
    @ManyToMany
    @JoinTable(
        name = "tourcategorytour",
        joinColumns = @JoinColumn(name = "packageid"),
        inverseJoinColumns = @JoinColumn(name = "categorytourid")
    )
    private List<CategoryTour> categoryTours;

    public List<CategoryTour> getCategoryTours() {
        return this.categoryTours;
    }

    public void setCategoryTours(List<CategoryTour> categoryTours) {
        this.categoryTours = categoryTours;
    }

    @ManyToMany
    @JoinTable(
        name = "tourthemetour",
        joinColumns = @JoinColumn(name = "packageid"),
        inverseJoinColumns = @JoinColumn(name = "themetourid")
    )
    private List<ThemeTour> themeTours;

    public List<ThemeTour> getThemeTours() {
        return this.themeTours;
    }

    public void setThemeTours(List<ThemeTour> themeTours) {
        this.themeTours = themeTours;
    }

    @ManyToMany
    @JoinTable(
        name = "toursuitabletour",
        joinColumns = @JoinColumn(name = "packageid"),
        inverseJoinColumns = @JoinColumn(name = "suitabletourid")
    )
    private List<SuitableTour> suitableTours;

    public List<SuitableTour> getSuitableTours() {
        return this.suitableTours;
    }

    public void setSuitableTours(List<SuitableTour> suitableTours) {
        this.suitableTours = suitableTours;
    }
    @ManyToMany
    @JoinTable(
        name = "tourdeparturedate",
        joinColumns = @JoinColumn(name = "packageid"),
        inverseJoinColumns = @JoinColumn(name = "departuredateid")
    )
    private List<DepartureDate> departureDates;

    public List<DepartureDate> getDepartureDate() {
        return this.departureDates;
    }

    public void setDepartureDate(List<DepartureDate> departureDate) {
        this.departureDates = departureDate;
    }

}

