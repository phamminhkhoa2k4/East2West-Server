package com.east2west.models.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "homestayamenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HomestayAmenities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homestayamenitiesid")
    private Integer homestayAmenitiesID;

    @ManyToOne
    @JoinColumn(name = "homestayid", referencedColumnName = "homestayid")
    private Homestay homestay;

    @ManyToOne
    @JoinColumn(name = "amenitiesid", referencedColumnName = "amenitiesid")
    private Amenities amenities;
}

