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

    @Column(name = "groupsize")
    private String groupsize;

    @Column(name = "deposit")
    private String deposit;

    @OneToMany(mappedBy = "tourPackage",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Itinerary> itineraries;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "themetour",
            joinColumns = @JoinColumn(name = "packageid"),
            inverseJoinColumns = @JoinColumn(name = "themeid")
    )
    private Set<Theme> themes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "suitabletour",
            joinColumns = @JoinColumn(name = "packageid"),
            inverseJoinColumns = @JoinColumn(name = "suitableid")
    )
    private Set<Suitable> suitable;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "categoriestour",
            joinColumns = @JoinColumn(name = "packageid"),
            inverseJoinColumns = @JoinColumn(name = "categoryid")
    )
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "packageid")
    private List<DepartureDate> departuredates;


}
