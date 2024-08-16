package com.east2west.models.Entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "itinerary")
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itineraryid")
    private int itineraryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageid", referencedColumnName = "packageid")
    @JsonIgnore
    private TourPackage tourPackage;

    @ManyToMany
    @JoinTable(
        name = "itineraryaccommodation",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "accommodationid")
    )
    private List<Accommodation> accommodations;

    public List<Accommodation> getAccommodations() {
        return this.accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    @ManyToMany
    @JoinTable(
        name = "itinerarymeals",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "mealid")
    )
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return this.meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @ManyToMany
    @JoinTable(
        name = "itineraryplaces",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "placeid")
    )
    private List<Place> places;

    public List<Place> getPlaces() {
        return this.places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Column(name = "day")
    private Timestamp day;

    // Getters and setters
    public int getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(int itineraryId) {
        this.itineraryId = itineraryId;
    }

    public TourPackage getTourPackage() {
        return tourPackage;
    }

    public void setTourPackage(TourPackage tourPackage) {
        this.tourPackage = tourPackage;
    }

    public Timestamp getDay() {
        return day;
    }

    public void setDay(Timestamp day) {
        this.day = day;
    }
}
