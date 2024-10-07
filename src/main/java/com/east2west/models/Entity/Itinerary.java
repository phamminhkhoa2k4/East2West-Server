package com.east2west.models.Entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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



    @ManyToMany
    @JoinTable(
        name = "itinerarymeals",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "mealid")
    )
    private List<Meal> meals;



    @ManyToMany
    @JoinTable(
        name = "itineraryplaces",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "placeid")
    )
    private List<Place> places;

    @ManyToMany
    @JoinTable(
        name = "itinerarytransfer",
        joinColumns = @JoinColumn(name = "itineraryid"),
        inverseJoinColumns = @JoinColumn(name = "transferid")
    )
    private List<Transfer> transfers;


    @Column(name = "day")
    private int day;

  

}
