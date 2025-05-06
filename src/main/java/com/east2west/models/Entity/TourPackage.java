package com.east2west.models.Entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.east2west.util.StringListConverter;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tourpackage")
public class TourPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packageid")
    private int packageid;



    @Column(name = "title")
    private String title;



    @Convert(converter = StringListConverter.class)
    @Column(name = "thumbnail", columnDefinition = "Text")
    private List<String> thumbnail;


    @Column(name = "price")
    private BigDecimal price;



//    @Column(name = "pricereduce")
//    private BigDecimal pricereduce;

//    public BigDecimal getPricereduce() {
//        return this.pricereduce;
//    }
//
//    public void setPricereduce(BigDecimal pricereduce) {
//        this.pricereduce = pricereduce;
//    }

    @Column(name = "groupsize")
    private String groupsize;

    @Column(name = "deposit")
    private String deposit;

    @Column(name = "bookinghold")
    private String bookinghold;

    @Column(name = "bookingchange")
    private String bookingchange;

    @OneToMany(mappedBy = "tourPackage", fetch = FetchType.LAZY)
    private List<Itinerary> itineraries;



    @ManyToMany
    @JoinTable(name = "tourcategorytour", joinColumns = @JoinColumn(name = "packageid"), inverseJoinColumns = @JoinColumn(name = "categorytourid"))
    private List<CategoryTour> categoryTours;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "themetour",
            joinColumns = @JoinColumn(name = "packageid"),
            inverseJoinColumns = @JoinColumn(name = "themeid")
    )
    private Set<Theme> themes = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "toursuitabletour", joinColumns = @JoinColumn(name = "packageid"), inverseJoinColumns = @JoinColumn(name = "suitabletourid"))
    private List<SuitableTour> suitableTours;



    @ManyToMany
    @JoinTable(name = "tourdeparturedate", joinColumns = @JoinColumn(name = "packageid"), inverseJoinColumns = @JoinColumn(name = "departuredateid"))
    private List<DepartureDate> departureDates;


}
