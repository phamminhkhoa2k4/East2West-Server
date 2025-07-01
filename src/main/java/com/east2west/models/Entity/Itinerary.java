package com.east2west.models.Entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "day")
    private int day;


    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityOrder> activityOrders = new ArrayList<>();
}
